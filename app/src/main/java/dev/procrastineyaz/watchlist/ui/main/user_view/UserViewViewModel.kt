package dev.procrastineyaz.watchlist.ui.main.user_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.ItemsQuery
import dev.procrastineyaz.watchlist.data.repositories.ItemsRepository
import dev.procrastineyaz.watchlist.ui.main.common.BaseItemsViewModel
import dev.procrastineyaz.watchlist.ui.main.common.ItemsAdapterProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserViewViewModel(itemsRepository: ItemsRepository, ) : BaseItemsViewModel(itemsRepository),
    ItemsAdapterProvider {

    var subscriptionUserId: Long? = null

    override fun getItemsFromRepo(query: ItemsQuery): LiveData<PagedList<Item>> {
        val userId = subscriptionUserId
        requireNotNull(userId)
        return itemsRepository.getSubscriptionItems(userId, query, viewModelScope)
    }
}
