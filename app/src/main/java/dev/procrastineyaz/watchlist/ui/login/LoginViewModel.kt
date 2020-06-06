package dev.procrastineyaz.watchlist.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.procrastineyaz.watchlist.data.repositories.UsersRepository
import dev.procrastineyaz.watchlist.services.TokenService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(
    private val usersRepository: UsersRepository,
    private val tokenService: TokenService
) : ViewModel() {

    private val _loginErrorMessage = MutableLiveData<String?>(null)
    val loginErrorMessage: LiveData<String?>
    get() = _loginErrorMessage

    private val _passwordErrorMessage = MutableLiveData<String?>(null)
    val passwordErrorMessage: LiveData<String?>
    get() = _passwordErrorMessage

    private val _progressState = MutableLiveData<LoginProgressState>(LoginProgressState.None)
    val progressState: LiveData<LoginProgressState>
    get() = _progressState

    private var login: String = ""
    private var password: String = ""

    fun onLoginClick() = viewModelScope.launch {
        try {
            _progressState.postValue(LoginProgressState.Loading)
            val loginToken = usersRepository.login(login, password)
            tokenService.token = loginToken.token
            delay(100)
            _progressState.postValue(LoginProgressState.Success)
        } catch (e: Exception) {
            _progressState.postValue(LoginProgressState.Error(e.message ?: "Unknown Error"))
        }

    }

    fun onLoginChanged(text: String) {
        login = text
    }

    fun onPasswordChanged(text: String) {
        password = text
    }
}
