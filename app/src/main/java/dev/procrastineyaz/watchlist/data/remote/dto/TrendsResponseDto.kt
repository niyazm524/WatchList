package dev.procrastineyaz.watchlist.data.remote.dto

data class TrendsResponseDto(
    override val count: Int,
    override val itemsPerPage: Int,
    val maxRank: Int,
    val trendItems: List<TrendItemDto>
) : IPageableResponse {
    override val items: List<RemoteItem>
    get() = trendItems.map { it.film }
}

data class TrendItemDto(val rank: Int, val film: RemoteItem)
