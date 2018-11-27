package net.android.anko.paging.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.PagedList
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.base.BaseViewModel
import net.android.anko.base.di.names.Remote
import net.android.anko.model.ApiGankRepository
import net.android.anko.model.model.GankModel
import net.android.anko.paging.GankRepository
import net.android.anko.paging.Listing
import net.android.anko.paging.NetworkState
import javax.inject.Inject


abstract class BasePagingViewModel<T> : BaseViewModel() {
    /**
     * map switchMap 将Listing中的引用给变量 上游setValue时,下游会接受到监听
     * 结果必须需要有observer才会进行onChanged,才会进行回调
     */
    var pagingList: LiveData<Listing<T>> = MediatorLiveData()


    var list: LiveData<PagedList<T>>? = null
    var networkState: LiveData<NetworkState>? = null
    var refreshState: LiveData<NetworkState>? = null
    var isRefresh = false

    fun initLiveData() {
        list = Transformations.switchMap(pagingList) {
            it.pagedList
        }!!
        networkState = Transformations.switchMap(pagingList) {
            it.networkState
        }!!
        refreshState = Transformations.switchMap(pagingList) {
            it.refreshState
        }!!
    }
    abstract fun initPagingList()

    fun retry() {
        val listing = pagingList.value
        listing?.retry?.invoke()
    }

    fun refresh() {
        isRefresh = true
        pagingList.value?.refresh?.invoke()
    }

    override fun onCleared() {
        super.onCleared()
        pagingList.value?.clear?.invoke()
    }

}
