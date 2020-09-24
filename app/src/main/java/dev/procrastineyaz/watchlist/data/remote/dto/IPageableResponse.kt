package dev.procrastineyaz.watchlist.data.remote.dto

import kotlin.math.ceil

interface IPageableResponse {
    val items: List<RemoteItem>
    val count: Int
    val itemsPerPage: Int

    fun pagesCount() = ceil(count.toDouble() / itemsPerPage).toInt()
}
