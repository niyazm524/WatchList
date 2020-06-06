package dev.procrastineyaz.watchlist.services

import android.content.Context

class TokenService(context: Context) {
    private val sharedPrefs = context.getSharedPreferences("user-info", Context.MODE_PRIVATE)
    private var _token: String?

    init {
        _token = sharedPrefs.getString("token", null)
    }

    var token: String?
    get() = _token
    set(value) {
        sharedPrefs.edit().putString("token", value).apply()
        _token = value
    }
}
