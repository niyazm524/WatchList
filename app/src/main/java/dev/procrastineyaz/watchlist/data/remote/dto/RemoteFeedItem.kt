package dev.procrastineyaz.watchlist.data.remote.dto

import dev.procrastineyaz.watchlist.data.dto.FeedItem
import dev.procrastineyaz.watchlist.data.dto.FeedType
import dev.procrastineyaz.watchlist.data.mappers.toItem

data class RemoteFeedItem(val id: Long, val feedType: Int, val user: UserPublicInfo, val item: RemoteItem) {
    fun toFeedItem(): FeedItem = FeedItem(
        id = id,
        feedType = FeedType.fromId(feedType),
        user = user,
        item = item.toItem(),
    )
}

data class FeedResponse(
    override val count: Int,
    override val itemsPerPage: Int,
    val items: List<RemoteFeedItem>,
) : IPageableResponse<FeedItem> {
    override fun getMappedItems(): List<FeedItem> {
        return items.map { it.toFeedItem() }
    }
}
