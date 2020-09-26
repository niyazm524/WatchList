package dev.procrastineyaz.watchlist.data.repositories.sources

import dev.procrastineyaz.watchlist.data.dto.FeedItem
import dev.procrastineyaz.watchlist.data.remote.FeedAPIService
import dev.procrastineyaz.watchlist.data.remote.dto.IPageableResponse
import kotlinx.coroutines.CoroutineScope

class FeedDataSource(scope: CoroutineScope, private val feedAPIService: FeedAPIService) :
    BaseItemsDataSource<FeedItem>(scope) {
    override suspend fun fetchItems(page: Int, itemsPerPage: Int): IPageableResponse<FeedItem> {
        return feedAPIService.fetchFeed(page, itemsPerPage)
    }
}
