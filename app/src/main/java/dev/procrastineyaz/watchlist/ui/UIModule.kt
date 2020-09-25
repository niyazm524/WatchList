package dev.procrastineyaz.watchlist.ui

import dev.procrastineyaz.watchlist.ui.dto.ItemsProviders
import dev.procrastineyaz.watchlist.ui.login.LoginViewModel
import dev.procrastineyaz.watchlist.ui.main.common.AddItemDialogViewModel
import dev.procrastineyaz.watchlist.ui.main.common.ItemsAdapter
import dev.procrastineyaz.watchlist.ui.main.feed.FeedViewModel
import dev.procrastineyaz.watchlist.ui.main.home.HomeViewModel
import dev.procrastineyaz.watchlist.ui.main.item_view.ItemDetailsViewModel
import dev.procrastineyaz.watchlist.ui.main.more.MoreViewModel
import dev.procrastineyaz.watchlist.ui.main.trends.TrendsViewModel
import dev.procrastineyaz.watchlist.ui.main.user_view.UserViewViewModel
import dev.procrastineyaz.watchlist.ui.main.users.UsersViewModel
import dev.procrastineyaz.watchlist.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val uiModule = module {
    viewModel { FeedViewModel() }
    viewModel(named(ItemsProviders.HomeItemsVM)) { HomeViewModel(get(), ItemsProviders.HomeItemsVM) }
    viewModel(named(ItemsProviders.UserItemsVM)) { HomeViewModel(get(), ItemsProviders.UserItemsVM) }
    viewModel { TrendsViewModel(get()) }
    viewModel { MoreViewModel(get()) }
    viewModel { UsersViewModel(get()) }
    viewModel { UserViewViewModel() }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { AddItemDialogViewModel(get()) }
    viewModel { ItemDetailsViewModel(get()) }
    factory { ItemsAdapter() }
}
