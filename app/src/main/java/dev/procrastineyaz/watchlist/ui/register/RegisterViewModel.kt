package dev.procrastineyaz.watchlist.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.procrastineyaz.watchlist.data.repositories.UsersRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.contracts.contract

class RegisterViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    val username = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val passwordRepeat = MutableLiveData<String>("")

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _credentialsIfRegistered = MutableLiveData<Pair<String, String>?>(null)
    val credentialsIfRegistered: LiveData<Pair<String, String>?> = _credentialsIfRegistered

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
        _isLoading.postValue(true)
        try {
            usersRepository.register(username, email, password)
            delay(100)
            _credentialsIfRegistered.postValue(username to password)
        } finally {
            _isLoading.postValue(false)
        }
    }

}
