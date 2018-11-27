package net.android.anko

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import net.android.anko.utils.extensions.handleE
import net.android.anko.utils.extensions.handleException
import org.jetbrains.anko.attempt
import org.jetbrains.anko.coroutines.experimental.bg
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import java.net.URL

class Test {

    @Test
    fun kotlin_test() {
        val shareUrl = "https://www.iesdouyin.com/share/video/6559002106999606532/?region=CN&mid=6416547849739176705&titleType=title_CN_1&utm_source=copy_link&utm_campaign=client_share&utm_medium=android&app=aweme&iid=33421751655&timestamp=1527156305"
        val douyin = douyin(shareUrl)
        println(douyin)
    }

    fun douyin(url: String): String? {
        val BASE_URL = "https://aweme.snssdk.com/aweme/v1/play/?video_id=%s"
        try {
            val html = URL(url)
                    .openStream()
                    .bufferedReader()
                    .use { it.readText() }
            val len = html.indexOf("video_id")
            val end = html.substring(len)
            val indexOf = end.indexOf("\",")
            var videoId = end.substring(9, indexOf)
            videoId = videoId.replace("\\u0026", "&")
            return String.format(BASE_URL, videoId)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    @Test
    fun tt() {
        runBlocking {
            println("111")
            for (x in 0..10000) {
                println("x")
            }
            println("222")
        }
        println("333")

        Thread.sleep(5000)
    }

    @Test
    fun t1() {
        foo()
        foo {}
        foo { println("Bar") }
    }

    val EMPTY: () -> Unit = {}
    var retry = EMPTY
    fun foo(onError: () -> Unit = EMPTY) {
        retry = onError
        if (retry === EMPTY) {
            // the default value is used
            println("EMPTY")
        } else {
            // a lambda was defined - no default value used
            println("UN EMPTY")
        }
    }

    suspend fun doAwait(deff: Deferred<Any>): Any {
        var result: Any
        try {
            result = deff.await()
            return result
        } catch (e: Exception) {
            println("有错误1111:" + e.toString())
        }
        deff.invokeOnCompletion {
            println("有错误:" + it.toString())
            deff.cancel()
        }
        return Unit
    }

    private val factory = CoroutineCallAdapterFactory()
    private val retrofit = Retrofit.Builder()
            .baseUrl("http://example.com")
            .callFactory { TODO() }
            .build()

    @Test
    fun noCancelOnResponse() {
        val deferredString = typeOf<Deferred<String>>()
        val adapter = factory.get(deferredString, emptyArray(), retrofit)!! as CallAdapter<String, Deferred<String>>
        val call = CompletableCall<String>()
        val deferred = adapter.adapt(call)
        call.complete("hey")
        assertFalse(call.isCanceled)
        assertTrue(deferred.isCompleted)
    }

    @Test
    fun noCancelOnError() {
        val deferredString = typeOf<Deferred<String>>()
        val adapter = factory.get(deferredString, emptyArray(), retrofit)!! as CallAdapter<String, Deferred<String>>
        val call = CompletableCall<String>()
        val deferred = adapter.adapt(call)
        call.completeWithException(IOException())
        assertFalse(call.isCanceled)
        assertTrue(deferred.isCompletedExceptionally)
    }

    @Test
    fun cancelOnCancel() {
        val deferredString = typeOf<Deferred<String>>()
        val adapter = factory.get(deferredString, emptyArray(), retrofit)!! as CallAdapter<String, Deferred<String>>
        val call = CompletableCall<String>()
        val deferred = adapter.adapt(call)
        assertFalse(call.isCanceled)
        deferred.cancel()
        assertTrue(call.isCanceled)
    }

    private inline fun <reified T> typeOf(): Type = object : TypeToken<T>() {}.type
}