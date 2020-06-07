package dev.procrastineyaz.watchlist.ui

import dev.procrastineyaz.watchlist.ui.login.LoginViewModel
import dev.procrastineyaz.watchlist.ui.main.common.FilmsAdapter
import dev.procrastineyaz.watchlist.ui.main.feed.FeedViewModel
import dev.procrastineyaz.watchlist.ui.main.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { FeedViewModel() }
    viewModel { HomeViewModel() }
    viewModel { LoginViewModel(get(), get()) }
    factory { FilmsAdapter() }
}
