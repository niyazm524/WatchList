package dev.procrastineyaz.watchlist.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import dev.procrastineyaz.watchlist.data.local.entities.ItemEntity

@Dao
interface ItemsDao {
    @Query("SELECT * FROM items ORDER BY createdAt DESC")
    suspend fun getPagedItems(): DataSource.Factory<Int, ItemEntity>

}
