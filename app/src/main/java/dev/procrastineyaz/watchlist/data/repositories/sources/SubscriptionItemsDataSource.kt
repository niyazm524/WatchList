package dev.procrastineyaz.watchlist.data.repositories.sources

import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.ItemsQuery
import dev.procrastineyaz.watchlist.data.remote.ItemsAPIService
import dev.procrastineyaz.watchlist.data.remote.dto.IPageableResponse
import kotlinx.coroutines.CoroutineScope

class SubscriptionItemsDataSource(
    scope: CoroutineScope,
    private val userId: Long,
    private val query: ItemsQuery,
    private val itemsAPIService: ItemsAPIService
) : BaseItemsDataSource<Item>(scope) {

    override suspend fun fetchItems(page: Int, itemsPerPage: Int): IPageableResponse<Item> {
        return itemsAPIService.getSubscriptionItems(
            userId = userId,
            category = query.category.toStringBackend(),
            filter = query.filter.seen,
            page = page,
            itemsPerPage = itemsPerPage
        )
    }

}
