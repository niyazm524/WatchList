package dev.procrastineyaz.watchlist.data.repositories

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.FeedItem
import dev.procrastineyaz.watchlist.data.dto.PagedItemsResult
import dev.procrastineyaz.watchlist.data.remote.FeedAPIService
import dev.procrastineyaz.watchlist.data.repositories.sources.FeedDataSource
import dev.procrastineyaz.watchlist.data.repositories.sources.ItemsSourceFactory
import kotlinx.coroutines.CoroutineScope

class FeedRepository(private val feedAPIService: FeedAPIService) {
    fun getSubscribers(scope: CoroutineScope): PagedItemsResult<FeedItem> {
        val factory = ItemsSourceFactory { FeedDataSource(scope, feedAPIService) }
        val pagedList = LivePagedListBuilder(
            factory,
            PagedList.Config.Builder()
                .setPageSize(20)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build()
        ).setInitialLoadKey(1).build()
        return PagedItemsResult(pagedList, factory.isLoading, factory.networkState)
    }
}
