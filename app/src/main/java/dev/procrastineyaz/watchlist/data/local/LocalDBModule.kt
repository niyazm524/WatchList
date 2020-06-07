package dev.procrastineyaz.watchlist.data.local

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDBModule = module {
    single<AppDatabase>(createdAtStart = true) {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .build()
    }
}
