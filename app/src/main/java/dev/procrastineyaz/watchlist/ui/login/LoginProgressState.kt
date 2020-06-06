package dev.procrastineyaz.watchlist.ui.login

sealed class LoginProgressState {
    object None : LoginProgressState()
    object Success : LoginProgressState()
    object Loading : LoginProgressState()
    class Error(val message: String) : LoginProgressState()
}
