package dev.procrastineyaz.watchlist.data.repositories

import org.koin.dsl.module

val repositoriesModule = module {
    single { UsersRepository(get()) }
    single { ItemsRepository(get(), get()) }
}
