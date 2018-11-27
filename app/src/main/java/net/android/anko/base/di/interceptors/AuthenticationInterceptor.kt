package net.android.anko.base.di.interceptors

import android.text.TextUtils
import net.android.anko.helper.PrefGetter
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = PrefGetter.getToken()
        var request = chain.request()
        if (!TextUtils.isEmpty(token)) {
            val auth = if (token!!.startsWith("Basic")) token else "token $token"
            request = request.newBuilder()
                    .addHeader("Authorization", auth)
                    .build()
        }
        return chain.proceed(request)
    }
}