package dev.procrastineyaz.watchlist.data.dto

enum class FeedType(val id: Short) {
    ITEM_WISH(0),
    ITEM_WATCHED(1),
    ITEM_RECOMMENDED(2);

    companion object {
        fun fromId(id: Int) = when(id) {
            0 -> ITEM_WISH
            1 -> ITEM_WATCHED
            else -> ITEM_RECOMMENDED
        }
    }
}
