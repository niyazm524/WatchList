package dev.procrastineyaz.watchlist.data.remote.dto

import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.mappers.toItem

data class TrendsResponseDto(
    override val count: Int,
    override val itemsPerPage: Int,
    val maxRank: Int,
    val trendItems: List<TrendItemDto>
) : IPageableResponse<Item> {
    override val items: List<Item>
    get() = trendItems.map { it.film.toItem() }
}

data class TrendItemDto(val rank: Int, val film: RemoteItem)
