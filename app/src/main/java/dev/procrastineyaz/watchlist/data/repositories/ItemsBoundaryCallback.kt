package dev.procrastineyaz.watchlist.data.repositories

import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.ItemsQuery
import dev.procrastineyaz.watchlist.data.local.dao.ItemsDao
import dev.procrastineyaz.watchlist.data.mappers.toEntityItem
import dev.procrastineyaz.watchlist.data.remote.ItemsAPIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemsBoundaryCallback(
    private val query: ItemsQuery,
    private val itemsDao: ItemsDao,
    private val itemsAPIService: ItemsAPIService,
    private val scope: CoroutineScope,
) : PagedList.BoundaryCallback<Item>() {
    private var count: Int = -1

    override fun onZeroItemsLoaded() {
        if(count == 0) { return }
        scope.launch(Dispatchers.IO) {
            val response = itemsAPIService.getItems(
                category = query.category.toStringBackend(),
                filter = query.filter.seen,
                page = 1,
                itemsPerPage = 20
            )
            count = response.count
            itemsDao.insertItems(response.items.map { it.toEntityItem() })
        }
    }

}
