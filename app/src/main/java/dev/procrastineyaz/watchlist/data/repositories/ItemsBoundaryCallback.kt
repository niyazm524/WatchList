package dev.procrastineyaz.watchlist.data.repositories

import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.ItemsQuery
import dev.procrastineyaz.watchlist.data.local.dao.ItemsDao
import dev.procrastineyaz.watchlist.data.mappers.toEntityItem
import dev.procrastineyaz.watchlist.data.remote.ItemsAPIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

class ItemsBoundaryCallback(
    private val query: ItemsQuery,
    private val itemsDao: ItemsDao,
    private val itemsAPIService: ItemsAPIService,
    private val scope: CoroutineScope,
) : PagedList.BoundaryCallback<Item>() {
    private var count: Int = -1
    private var isRunning: Boolean = false
    private var page = 0

    override fun onZeroItemsLoaded() {
        if(count == 0 || isRunning) { return }
        isRunning = true
        scope.launch(Dispatchers.IO) {
            invalidateItems(page = ++page)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Item) {
        if(isRunning) return
        if(count != -1 && page * 20 < count) return
        scope.launch(Dispatchers.IO) {
            invalidateItems(page = ++page)
        }
    }

    private suspend fun invalidateItems(page: Int, wait: Long = 800) {
        try {
            val response = itemsAPIService.getItems(
                category = query.category.toStringBackend(),
                filter = query.filter.seen,
                page = page,
                itemsPerPage = 20
            )
            count = response.count
            itemsDao.insertItems(response.items.map { it.toEntityItem() })
        } catch (error: Throwable) {
            delay(wait)
            invalidateItems(page, min(wait * 2, 30000))
        } finally {
            isRunning = false
        }
    }

}
