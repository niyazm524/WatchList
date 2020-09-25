package dev.procrastineyaz.watchlist.ui.main.user_view

import android.os.Bundle
import dev.procrastineyaz.watchlist.ui.dto.ItemsProviders
import dev.procrastineyaz.watchlist.ui.main.common.BaseItemsFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UserItemsFragment : BaseItemsFragment() {
    val vm: UserViewViewModel by sharedViewModel()
    override val baseVM
    get() = vm
    override val provider = ItemsProviders.UserItemsVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.subscriptionUserId = arguments?.getLong("userId")
    }
}
