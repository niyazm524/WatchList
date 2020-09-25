package dev.procrastineyaz.watchlist.services

import android.content.Context
import io.objectbox.BoxStore

class TokenService(context: Context, private val boxStore: BoxStore) {
    private val sharedPrefs = context.getSharedPreferences("user-info", Context.MODE_PRIVATE)
    private var _token: String?
    private var _username: String?

    init {
        _token = sharedPrefs.getString("token", null)
        _username = sharedPrefs.getString("username", null)
    }

    fun eraseDB() {
        boxStore.removeAllObjects()
    }

    var username: String?
    get() = _username
    set(value) {
        sharedPrefs.edit().putString("username", value).apply()
        _username = value
    }

    var token: String?
    get() = _token
    set(value) {
        sharedPrefs.edit().putString("token", value).apply()
        _token = value
        if(value == null) {
            username = null
            eraseDB()
        }
    }
}
