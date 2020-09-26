package dev.procrastineyaz.watchlist.data.dto

import dev.procrastineyaz.watchlist.data.remote.dto.UserPublicInfo

data class FeedItem(val id: Long, val feedType: FeedType, val user: UserPublicInfo, val item: Item) {
    fun getFeedText(): String = when(feedType) {
        FeedType.ITEM_WISH -> "${user.username} хочет посмотреть ${item.getCategoryName()}"
        FeedType.ITEM_WATCHED -> "${user.username} посмотрел ${item.getCategoryName()}"
        FeedType.ITEM_RECOMMENDED -> "${user.username} рекомендует к просмотру ${item.getCategoryName()}"
    }
}
