package net.android.anko.utils.extensions

import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import okhttp3.ResponseBody
import retrofit2.Response

suspend fun <T : Response<R>, R> Deferred<T>.handleException(block: (ResponseBody) -> Unit): R {
    val t = await()
    if (t.isSuccessful) {
        return t.body()!!
    } else {
        block.invoke(t.errorBody()!!)
        println("--------------throw exception")
//        throw Exception(t.errorBody()?.string())
        return t.body()!!
    }
}

suspend fun <T> Deferred<T>.handleE(block: () -> Unit): T {
    try {
        return await()
    } catch (e: Exception) {
        block.invoke()
        println("--------------throw exception")
        throw Exception("error")
    }
}

