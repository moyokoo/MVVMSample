package net.android.anko.paging.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.paging.NetworkState

/**
 *
 * 当滑动到你指定的itemCount时,会自动加载数据,调用loadAfter()方法,默认是为你的设置的pageSize大小
 * 比如设置了pageSize为20,那么剩余item为20时,就会调用这个方法
 * 1. LivePagedListBuilder-build(根据Factory和DataSource来构建包含数据源LiveData的PageList)
 * 2. create ComputableLiveData(创建的时候就会执行mRefreshRunnable)-create new LiveData and return
 * 2.1 mRefreshRunnable子线程完成计算工作后,调用mLiveData.postValue(value) View层的Observer则会接收到结果
 * 2.2 计算工作主要是创建PageList,只会计算一次,计算完后会返回List
 * 3. PageList-build-create
 * 4. create ContiguousPagedList or TiledPagedList(if isContiguous is true) 如果保证数据的item数不会变化,则可以设置这个属性
 * 5. dispatchLoadInitial
 * 6. create LoadInitialCallbackImpl
 * 7. loadInitial
 * 7.1 如果此时加载数据失败可以调用loadInitial()重新进行请求
 * 8. callBack.onResult()
 * 9. 回调至LoadInitialCallbackImpl
 * 10. 根据原数据重新创建PageList,调用dispatchResultToReceiver
 * 11. PageResult.Receiver()根据PageResult的不同状态处理不同情况
 * 11.1 第一次显示列表状态为 PageResult.INIT,后续加载数据的状态为PageResult.APPEND,进行一些回调工作(onChanged,onInserted,onRemoved等) TODO PageResult.PREPEND
 * 11.2 由于在loadInitial方法中,我们的请求时同步的,所以会在数据处理结束后,View层的LiveData才会接受到数据
 * 12. 列表初始显示、滑动或者notifyDataSetChanged时,会调用Adapter的getItem
 * 13. 委托给AsyncPagedListDiffer的getItem
 * 14. mPagedList.loadAround(index)-loadAroundInternal(这里根据设置的prefetchDistance设置加载到多少item时去加载新数据)
 * 15. scheduleAppend-dispatchLoadAfter(所以当新数据加载时,会调用loadAfter()方法)
 *
 * KEY主要是给用户处理,Page可以设置PreviousKey和NextKey,分别在loadBefore和loadAfter的参数中可以获取
 * ItemKeyedDataSource的KEY则是统一,每个方法获取到的key一致,传空字符串也是可以的
 * 如果没有特殊要求,一般都是传Page的页数
 */
abstract class BaseItemDataSource<V> : PageKeyedDataSource<Int, V>() {
    val lock = Any()
    private var deferredMap = mutableMapOf<Int, Job>()
    var retry: (() -> Any)? = null
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _initialLoad = MutableLiveData<NetworkState>()
    val initialLoad: MutableLiveData<NetworkState>
        get() = _initialLoad
    var currentPage = 1
    var nextPage = currentPage + 1

    fun retryFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            launch {
                prevRetry()
            }
        }
    }

    fun clear() {
        synchronized(lock) {
            for (key in deferredMap.keys) {
                deferredMap[key]?.cancel()
            }
        }
        deferredMap.clear()
    }


    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, V>) {

    }

    /**
     *     refresh = {
     *              sourceFactory.sourceLiveData.value?.invalidate()
     *      },
     *     private final DataSource.InvalidatedCallback mCallback =
     *          new DataSource.InvalidatedCallback() {
     *          @Override
     *          public void onInvalidated() {
     *              invalidate();
     *          }
     *      };
     *       调用刷新方法invalidate后,会先将数据清除,然后数据的LiveData会进行postValue
     *       为了保证该LiveData是获取到数据之后再进行post,这里的网络操作需要同步
     *       在compute的计算方法中,会将pageList重新实例化,会优先调用loadInitial方法进行初始化,实例化后,将结果返回给compute()
     *       在dispatchLoadInitial方法中会进行postExecutor的设置,如果loadInitial方法是异步的,postExecutor就会优先设置
     *
     *    if (executor != null) {
     *         executor.execute(new Runnable() {
     *       @Override
     *        public void run() {
     *            mReceiver.onPageResult(mResultType, result);
     *            }
     *        });
     *        } else {
     *            mReceiver.onPageResult(mResultType, result);
     *        }
     *      就会导致数据不正常(页面数据会先消失,即页面的item被隐藏,在数据加载后才显示)
     */
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, V>) {
        _networkState.postValue(NetworkState.LOADING)
        _initialLoad.postValue(NetworkState.LOADING)
        currentPage = 1
        nextPage = currentPage + 1
        runBlocking {
            try {
                //先进行运算获取结果 需要先提交数据给adapter  再更新状态  否则会出现位置错位
                val result = onLoadInitial(params, callback)
                _networkState.postValue(NetworkState.LOADED)
                _initialLoad.postValue(NetworkState.LOADED)
                retry = null
                callback.onResult(result, null, nextPage)
            } catch (e: Exception) {
                retry = { loadInitial(params, callback) }
                val error = NetworkState.error(e)
                LogUtils.e("initialLoad error:" + e + "error message result:" + error)
                _networkState.postValue(error)
                _initialLoad.postValue(error)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, V>) {


        currentPage = params.key
        nextPage = currentPage + 1
        _networkState.postValue(NetworkState.LOADING)
        val job = launch {
            try {
                val result = onLoadAfter(params, callback)
                retry = null
                callback.onResult(result, nextPage)
                _networkState.postValue(NetworkState.LOADED)
            } catch (e: Exception) {
                retry = { loadAfter(params, callback) }
                LogUtils.e("after error:" + e + "error message result:" + e)
                _networkState.postValue(NetworkState.error(e))
            }
        }
        job.invokeOnCompletion {
            synchronized(lock) {
                deferredMap.remove(job.hashCode())
            }
        }
        deferredMap[job.hashCode()] = job
    }


    abstract suspend fun onLoadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, V>): List<V>


    abstract suspend fun onLoadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, V>): List<V>


    fun onError(throwable: Throwable) {
        LogUtils.e("Throwable:" + throwable.toString())
    }


}