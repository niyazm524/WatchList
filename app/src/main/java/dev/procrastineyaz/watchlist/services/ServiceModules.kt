package dev.procrastineyaz.watchlist.services

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val serviceModules = module {
    single(createdAtStart = true) { TokenService(androidContext(), get()) }
}
