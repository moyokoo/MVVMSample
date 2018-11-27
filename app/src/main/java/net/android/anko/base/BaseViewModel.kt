package net.android.anko.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.utils.extensions.default
import org.jetbrains.anko.AnkoLogger


/**
 * Created by miaoyongjun
 */

open class BaseViewModel : ViewModel(), AnkoLogger {

    val lock = Any()
    var deferredMap = mutableMapOf<Int, Job>()
    val errorLiveData by lazy { MutableLiveData<Throwable>() }
    val showErrorMessage by lazy { MutableLiveData<Any>() }
    val showMessage by lazy { MutableLiveData<Any>() }
    val progressStatus by lazy { MutableLiveData<ProStatus>().default(ProStatus.NOTHING) }


    override fun onCleared() {
        //avoid ConcurrentModificationException
        synchronized(lock) {
            for (key in deferredMap.keys) {
                deferredMap[key]?.cancel()
            }
        }
        deferredMap.clear()
        super.onCleared()
    }


    fun onError(throwable: Throwable) {
        LogUtils.e("Throwable:" + throwable.toString())
        launch(UI) {
            errorLiveData.value = throwable
        }
    }


    fun <T> makeRestCall(deferred: Deferred<T>, next: (T) -> Unit = {}, error: (e: Exception) -> Unit = {},
                         complete: () -> Unit = {}) {
        val job = launch {
            try {
                val data = deferred.await()
                //invoke invokeOnCompletion
                launch(UI) {
                    next(data)
                }
            } catch (exception: Exception) {
                onError(exception)
                launch(UI) {
                    error(exception)
                }
            }
        }
        job.invokeOnCompletion {
            synchronized(lock) {
                deferredMap.remove(job.hashCode())
            }
            launch(UI) {
                complete()
            }
        }
        deferredMap[job.hashCode()] = job
    }
}
