package dev.procrastineyaz.watchlist.ui

import dev.procrastineyaz.watchlist.ui.common.FilmsAdapter
import dev.procrastineyaz.watchlist.ui.feed.FeedViewModel
import dev.procrastineyaz.watchlist.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { FeedViewModel() }
    viewModel { HomeViewModel() }
    factory { FilmsAdapter() }
}
