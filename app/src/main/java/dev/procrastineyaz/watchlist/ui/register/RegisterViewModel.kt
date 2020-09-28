package dev.procrastineyaz.watchlist.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.procrastineyaz.watchlist.data.dto.Result
import dev.procrastineyaz.watchlist.data.repositories.UsersRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

typealias Credentials = Pair<String, String>

class RegisterViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    val username = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordRepeat = MutableLiveData("")

    private val _result = MutableLiveData<Result<Credentials, Throwable>>()
    val result: LiveData<Result<Credentials, Throwable>> = _result

    private fun validateInput(): Boolean {
        return !username.value.isNullOrEmpty() && !email.value.isNullOrEmpty()
                && !password.value.isNullOrEmpty() && !passwordRepeat.value.isNullOrEmpty()
                && password.value == passwordRepeat.value
    }

    fun onRegisterBtnClick() {
        if(!validateInput()) {
            return
        }
        register()
    }

    private fun register() = viewModelScope.launch {
        val username = requireNotNull(username.value)
        val email = requireNotNull(email.value)
        val password = requireNotNull(password.value)
        _result.postValue(Result.Loading)
        try {
            usersRepository.register(username, email, password)
            delay(100)
            _result.postValue(Result.Success(username to password))
        } catch (err: Throwable) {
            _result.postValue(Result.Error(err))
        }
    }

}
