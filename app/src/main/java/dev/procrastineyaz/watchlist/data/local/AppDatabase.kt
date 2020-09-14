package dev.procrastineyaz.watchlist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.procrastineyaz.watchlist.data.local.dao.ItemsDao
import dev.procrastineyaz.watchlist.data.local.entities.ItemEntity
import dev.procrastineyaz.watchlist.data.local.typeconverters.CategoryConverter

@Database(entities = [ItemEntity::class], version = 1)
@TypeConverters(CategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemsDao(): ItemsDao

    companion object {
        const val DATABASE_NAME = "database.db"
    }
}
