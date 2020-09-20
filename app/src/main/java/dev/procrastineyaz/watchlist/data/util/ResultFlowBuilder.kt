package dev.procrastineyaz.watchlist.data.util

import dev.procrastineyaz.watchlist.data.dto.Result
import kotlinx.coroutines.flow.flow

fun <T> wrapInResultFlow(block: suspend () -> T) = flow {
    try {
        emit(Result.Loading)
        emit(Result.Success(block()))
    } catch (err: Throwable) {
        emit(Result.Error(err))
    }
}
