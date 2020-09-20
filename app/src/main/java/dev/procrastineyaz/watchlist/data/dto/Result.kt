package dev.procrastineyaz.watchlist.data.dto

sealed class Result<out T, out E> {
    data class Success<T>(val value: T): Result<T, Nothing>()
    data class Error<E>(val value: E): Result<Nothing, E>()
    object Loading: Result<Nothing, Nothing>()
}
