package dev.procrastineyaz.watchlist.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "abc"
        var request = chain.request()
        request = request.newBuilder().header("Authentication", "Bearer $token").build()
        return chain.proceed(request)
    }
}
