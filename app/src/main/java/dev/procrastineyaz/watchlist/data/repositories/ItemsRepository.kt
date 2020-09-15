package dev.procrastineyaz.watchlist.data.repositories


import android.os.Looper
import androidx.paging.DataSource
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.ItemsQuery
import dev.procrastineyaz.watchlist.data.local.dao.ItemsDao
import dev.procrastineyaz.watchlist.data.mappers.toItem
import dev.procrastineyaz.watchlist.data.remote.ItemsAPIService
import dev.procrastineyaz.watchlist.data.util.MainThreadExecutor
import io.objectbox.android.ObjectBoxDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ItemsRepository(
    private val itemsAPIService: ItemsAPIService,
    private val itemsDao: ItemsDao,
) {
    private val mainExecutor = MainThreadExecutor()
    private val ioExecutor = Executors.newSingleThreadExecutor()

    fun getItems(itemsQuery: ItemsQuery): Flow<PagedList<Item>> = flow {
        suspend fun emitFromDataSource(dataSource: DataSource<Int, Item>) {
            emit(
                PagedList.Builder(dataSource, 20)
                    .setNotifyExecutor(mainExecutor)
                    .setFetchExecutor(ioExecutor)
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
                    val callback = object : DataSource.InvalidatedCallback {
                        override fun onInvalidated() {
                            coroutine.resume(this)
                        }
                    }
                    dataSource.addInvalidatedCallback(callback)
                }
            } finally {
                if (callback != null) {
                    dataSource.removeInvalidatedCallback(callback)
                }
            }
        }
    }

}
