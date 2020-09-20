package dev.procrastineyaz.watchlist.data.repositories


import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.*
import dev.procrastineyaz.watchlist.data.local.dao.ItemsDao
import dev.procrastineyaz.watchlist.data.mappers.toItem
import dev.procrastineyaz.watchlist.data.remote.ItemsAPIService
import dev.procrastineyaz.watchlist.data.remote.dto.NewItemDto
import dev.procrastineyaz.watchlist.data.util.MainThreadExecutor
import dev.procrastineyaz.watchlist.data.util.wrapInResultFlow
import io.objectbox.android.ObjectBoxDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ItemsRepository(
    private val itemsAPIService: ItemsAPIService,
    private val itemsDao: ItemsDao,
) {
    private val mainExecutor = MainThreadExecutor()
    private val ioExecutor = Executors.newSingleThreadExecutor()

    fun getItems(itemsQuery: ItemsQuery, scope: CoroutineScope): Flow<PagedList<Item>> = flow {
        val boundaryCallback = ItemsBoundaryCallback(itemsQuery, itemsDao, itemsAPIService, scope)
        suspend fun emitFromDataSource(dataSource: DataSource<Int, Item>) {
            emit(
                PagedList.Builder(dataSource, 20)
                    .setNotifyExecutor(mainExecutor)
                    .setFetchExecutor(ioExecutor)
                    .setBoundaryCallback(boundaryCallback)
                    .build()
            )
        }
        val factory =
            ObjectBoxDataSource.Factory(itemsDao.getItems(itemsQuery)).map { itemEntity ->
                itemEntity.toItem()
            }
        while (true) {
            val dataSource = factory.create()
            var callback: DataSource.InvalidatedCallback? = null
            try {
                emitFromDataSource(dataSource)
                callback = suspendCoroutine<DataSource.InvalidatedCallback> { coroutine ->
                    val cb = object : DataSource.InvalidatedCallback {
                        override fun onInvalidated() {
                            coroutine.resume(this)
                        }
                    }
                    dataSource.addInvalidatedCallback(cb)
                }
            } finally {
                if (callback != null) {
                    dataSource.removeInvalidatedCallback(callback)
                }
            }
        }
    }

    fun searchItems(
        scope: CoroutineScope,
        query: String,
        category: Category
    ): PagedItemsResult<Item> {
        val isLoading = MutableLiveData(false)
        val networkState = MutableLiveData<NetworkState>(NetworkState.Success)
        val builder = LivePagedListBuilder(object : DataSource.Factory<Int, Item>() {
            override fun create(): DataSource<Int, Item> {
                return ItemsDataSource(
                    scope,
                    query,
                    category,
                    itemsAPIService,
                    isLoading::postValue,
                    networkState::postValue
                )
            }
        }, 20)
        builder.setFetchExecutor(ioExecutor)
        builder.setInitialLoadKey(1)
        return PagedItemsResult(builder.build(), isLoading, networkState)
    }

    fun addItem(
        itemId: Int,
        seen: Boolean = false,
        rating: Int? = null,
        note: String? = null
    ): Flow<Result<Item, Throwable>> = wrapInResultFlow {
        itemsAPIService.addItem(NewItemDto(itemId, seen, rating, note)).toItem()
    }

}
