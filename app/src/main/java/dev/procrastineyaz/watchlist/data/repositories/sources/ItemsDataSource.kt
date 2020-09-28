package dev.procrastineyaz.watchlist.data.repositories.sources

import android.util.Log
import androidx.paging.PageKeyedDataSource
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.NetworkState
import dev.procrastineyaz.watchlist.data.dto.Result
import dev.procrastineyaz.watchlist.data.mappers.toItem
import dev.procrastineyaz.watchlist.data.remote.ItemsAPIService
import dev.procrastineyaz.watchlist.data.remote.dto.ItemsSearchResponseDto
import dev.procrastineyaz.watchlist.data.remote.dto.RemoteItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemsDataSource(
    private val scope: CoroutineScope,
    private val query: String,
    private val category: Category,
    private val itemsAPIService: ItemsAPIService,
    private val setLoading: (loading: Boolean) -> Unit,
    private val setNetworkState: (state: NetworkState) -> Unit,
) : PageKeyedDataSource<Int, Item>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Item>
    ) {
        scope.launch {
            setLoading(true)
            val result = fetchItems(query)
            setLoading(false)
            Log.w("LOAD", result::javaClass.name)
            if (result is Result.Success) {
                callback.onResult(
                    mapListToItems(result.value.items),
                    0,
                    result.value.count,
                    null,
                    if (result.value.pagesCount > 1) 2 else -1
                )
            }
        }
    }


    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        if(params.key == -1) {
            return
        }
        scope.launch {
            val result = fetchItems(query, params.key)
            if (result is Result.Success) {
                callback.onResult(
                    mapListToItems(result.value.items),
                    if (result.value.pagesCount > params.key) params.key + 1 else -1
                )
            }
        }
    }

    private suspend fun fetchItems(
        query: String,
        page: Int = 1
    ): Result<ItemsSearchResponseDto, Throwable> {
        val result = withContext(Dispatchers.IO) {
            try {
                val response = itemsAPIService.searchItems(query, page)
                setNetworkState(NetworkState.Success)
                return@withContext Result.Success(response)
            } catch (err: Throwable) {
                setNetworkState(NetworkState.Failure(err))
                return@withContext Result.Error(err)
            }
        }
        return result
    }

    private fun mapListToItems(list: List<RemoteItem>): List<Item> {
        val items = list.map { it.toItem() }
        if(category == Category.UNKNOWN) return items
        return items.filter { item -> item.categoryId == category.value }
    }
}
