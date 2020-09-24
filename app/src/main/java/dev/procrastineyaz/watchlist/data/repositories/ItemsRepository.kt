package dev.procrastineyaz.watchlist.data.repositories


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.*
import dev.procrastineyaz.watchlist.data.local.dao.ItemsDao
import dev.procrastineyaz.watchlist.data.mappers.toItem
import dev.procrastineyaz.watchlist.data.remote.ItemsAPIService
import dev.procrastineyaz.watchlist.data.remote.dto.NewItemDto
import dev.procrastineyaz.watchlist.data.remote.dto.UserItemPropsDto
import dev.procrastineyaz.watchlist.data.util.MainThreadExecutor
import dev.procrastineyaz.watchlist.data.util.wrapInResultFlow
import io.objectbox.android.ObjectBoxDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors

class ItemsRepository(
    private val itemsAPIService: ItemsAPIService,
    private val itemsDao: ItemsDao,
) {
    private val mainExecutor = MainThreadExecutor()
    private val ioExecutor = Executors.newSingleThreadExecutor()

    fun getItems(itemsQuery: ItemsQuery, scope: CoroutineScope): LiveData<PagedList<Item>> {
        val boundaryCallback = ItemsBoundaryCallback(itemsQuery, itemsDao, itemsAPIService, scope)
        val factory = ObjectBoxDataSource.Factory(itemsDao.getItems(itemsQuery)).map { itemEntity ->
            itemEntity.toItem()
        }
        return LivePagedListBuilder(factory, 20)
            .setInitialLoadKey(1)
            .setBoundaryCallback(boundaryCallback)
            .build()
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
    ): Flow<Result<Any?, Throwable>> = wrapInResultFlow {
        itemsAPIService.addItem(NewItemDto(itemId, seen, rating, note))
    }

    fun updateItem(
        itemId: Int,
        seen: Boolean? = null,
        rating: Float? = null,
        note: String? = null
    ): Flow<Result<Boolean, Throwable>> = wrapInResultFlow {
        try {
            val result = itemsAPIService.updateItem(itemId, UserItemPropsDto(rating, note, seen))
            if(result) {
                itemsDao.updateItem(itemId, rating, seen, note)
            }
            return@wrapInResultFlow result
        } catch (err: Throwable) {
            throw err
        }
    }

    fun deleteItem(itemId: Int): Flow<Result<Unit, Throwable>> = wrapInResultFlow {
        try {
            itemsAPIService.deleteItem(itemId)
            itemsDao.deleteItem(itemId)
        } catch (err: Throwable) {
            throw err
        }
    }

}
