package dev.procrastineyaz.watchlist.ui

import dev.procrastineyaz.watchlist.ui.login.LoginViewModel
import dev.procrastineyaz.watchlist.ui.main.common.ItemsAdapter
import dev.procrastineyaz.watchlist.ui.main.feed.FeedViewModel
import dev.procrastineyaz.watchlist.ui.main.home.HomeViewModel
import dev.procrastineyaz.watchlist.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { FeedViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    factory { ItemsAdapter() }
}
