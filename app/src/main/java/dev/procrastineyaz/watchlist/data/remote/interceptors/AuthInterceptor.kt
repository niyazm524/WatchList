package dev.procrastineyaz.watchlist.data.remote.interceptors

import dev.procrastineyaz.watchlist.services.TokenService
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenService: TokenService) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenService.token ?: return chain.proceed(chain.request())
        var request = chain.request()
        request = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
