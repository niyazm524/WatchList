package dev.procrastineyaz.watchlist.data.repositories

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.PagedItemsResult
import dev.procrastineyaz.watchlist.data.remote.TrendsAPIService
import dev.procrastineyaz.watchlist.data.repositories.sources.ItemsSourceFactory
import dev.procrastineyaz.watchlist.data.repositories.sources.TrendItemsDataSource
import kotlinx.coroutines.CoroutineScope

class TrendsRepository(private val trendsAPIService: TrendsAPIService) {

    fun getTrends(scope: CoroutineScope, category: Category): PagedItemsResult<Item> {
        val factory = ItemsSourceFactory { TrendItemsDataSource(scope, category, trendsAPIService) }
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
