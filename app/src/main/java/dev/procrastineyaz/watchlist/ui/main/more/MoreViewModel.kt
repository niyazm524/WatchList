package dev.procrastineyaz.watchlist.ui.main.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.procrastineyaz.watchlist.services.TokenService

class MoreViewModel(private val tokenService: TokenService) : ViewModel() {

    private val _isLogout = MutableLiveData(false)
    val isLogout: LiveData<Boolean> = _isLogout

    fun onLogout() {
        tokenService.token = null
        _isLogout.postValue(true)
    }
}
