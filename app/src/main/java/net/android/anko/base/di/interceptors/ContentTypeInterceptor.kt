package net.android.anko.base.di.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class ContentTypeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request.newBuilder()
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("Content-type", "application/vnd.github.v3+json")
                .method(request.method(), request.body())
                .build())
    }
}