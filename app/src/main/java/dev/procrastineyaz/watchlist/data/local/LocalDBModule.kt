package dev.procrastineyaz.watchlist.data.local

import dev.procrastineyaz.watchlist.data.local.dao.ItemsDao
import dev.procrastineyaz.watchlist.data.local.entities.ItemEntity
import dev.procrastineyaz.watchlist.data.local.entities.MyObjectBox
import io.objectbox.Box
import io.objectbox.BoxStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDBModule = module(createdAtStart = true) {
    single<BoxStore> {
        MyObjectBox.builder()
            .androidContext(androidContext())
            .build()
    }

    single<Box<ItemEntity>> { get<BoxStore>().boxFor(ItemEntity::class.java) }

    single { ItemsDao(get()) }
}
