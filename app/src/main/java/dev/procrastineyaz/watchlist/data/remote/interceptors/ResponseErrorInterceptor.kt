package dev.procrastineyaz.watchlist.data.remote.interceptors

import dev.procrastineyaz.watchlist.data.remote.dto.ServerError
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject

class ResponseErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.isSuccessful) {
            return response
        }
        try {
            val body = response.body?.string() ?: return response
            val error = JSONObject(body).let { json ->
                if (!json.has("message")) return response
                ServerError(json.getString("message"), response.code)
            }
            throw error
        } catch (err: org.json.JSONException) {
            return response
        }
    }
}
