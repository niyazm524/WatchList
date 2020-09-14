package dev.procrastineyaz.watchlist.data.repositories

import androidx.paging.DataSource
import androidx.room.Query
import dev.procrastineyaz.watchlist.data.local.dao.ItemsDao
import dev.procrastineyaz.watchlist.data.local.entities.ItemEntity
import dev.procrastineyaz.watchlist.data.remote.ItemsAPIService

class ItemsRepository(
    private val itemsAPIService: ItemsAPIService,
    private val itemsDao: ItemsDao
) {

    suspend fun getItems() {
        val dataSourceFactory = itemsDao.getPagedItems()
        
    }

}
