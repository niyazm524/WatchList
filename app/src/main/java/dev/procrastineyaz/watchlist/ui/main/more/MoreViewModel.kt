package dev.procrastineyaz.watchlist.ui.main.more

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.procrastineyaz.watchlist.data.remote.UsersAPIService
import dev.procrastineyaz.watchlist.data.remote.dto.RemoteUser
import dev.procrastineyaz.watchlist.services.TokenService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoreViewModel(
    private val tokenService: TokenService,
    private val usersAPIService: UsersAPIService
) : ViewModel() {
    private val _userData = MutableLiveData<RemoteUser?>()
    val userData: LiveData<RemoteUser?> = _userData
    private val _isLogout = MutableLiveData(false)
    val isLogout: LiveData<Boolean> = _isLogout

    fun getUserData() = viewModelScope.launch {
        try {
            _userData.postValue(withContext(Dispatchers.IO) { usersAPIService.getSelf() })
        } catch (err: Throwable) {
            Log.w("userData", err.localizedMessage ?: err.toString())
        }
    }

    fun onLogout() {
        tokenService.token = null
        _isLogout.postValue(true)
    }
}
