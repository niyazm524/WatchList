package dev.procrastineyaz.watchlist.data.dto

sealed class NetworkState {
    object Success: NetworkState()
    data class Failure(val error: Throwable): NetworkState()
}
