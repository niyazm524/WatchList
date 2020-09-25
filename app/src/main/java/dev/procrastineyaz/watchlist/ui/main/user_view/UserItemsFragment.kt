package dev.procrastineyaz.watchlist.ui.main.user_view

import android.os.Bundle
import dev.procrastineyaz.watchlist.ui.dto.ItemsProviders
import dev.procrastineyaz.watchlist.ui.main.common.BaseItemsFragment
import dev.procrastineyaz.watchlist.ui.main.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier.named

class UserItemsFragment : BaseItemsFragment() {
    override val vm: HomeViewModel by sharedViewModel(named(ItemsProviders.UserItemsVM))
    override val provider = ItemsProviders.UserItemsVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.subscriptionUserId = arguments?.getLong("userId")
    }
}
