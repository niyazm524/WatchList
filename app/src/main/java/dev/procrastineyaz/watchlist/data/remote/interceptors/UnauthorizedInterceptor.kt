package dev.procrastineyaz.watchlist.data.remote.interceptors

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import dev.procrastineyaz.watchlist.services.TokenService
import dev.procrastineyaz.watchlist.ui.login.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response

class UnauthorizedInterceptor(
    private val tokenService: TokenService,
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if(response.code == 401) {
            tokenService.token = null
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Вы не авторизованы", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, LoginActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
        return response
    }
}
