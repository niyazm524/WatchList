package dev.procrastineyaz.watchlist.data.repositories.sources

import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.remote.TrendsAPIService
import dev.procrastineyaz.watchlist.data.remote.dto.IPageableResponse
import kotlinx.coroutines.CoroutineScope

class TrendItemsDataSource(
    scope: CoroutineScope,
    private val category: Category,
    private val trendsAPIService: TrendsAPIService
) : BaseItemsDataSource<Item>(scope) {

    override suspend fun fetchItems(page: Int, itemsPerPage: Int): IPageableResponse<Item> {
        return trendsAPIService.getTrends(
            page = page,
            itemsPerPage = itemsPerPage,
            category = category.toStringBackend()
        )
    }

}
