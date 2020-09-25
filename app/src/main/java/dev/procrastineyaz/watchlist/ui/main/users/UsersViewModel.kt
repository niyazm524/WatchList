package dev.procrastineyaz.watchlist.ui.main.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.NetworkState
import dev.procrastineyaz.watchlist.data.dto.SubscribeUser
import dev.procrastineyaz.watchlist.data.dto.UserType
import dev.procrastineyaz.watchlist.data.repositories.SubscribersRepository
import dev.procrastineyaz.watchlist.ui.helpers.pickFrom

class UsersViewModel(private val subscribersRepository: SubscribersRepository) : ViewModel() {
    private val _users = MediatorLiveData<PagedList<SubscribeUser>>()
    val users: LiveData<PagedList<SubscribeUser>> = _users
    val networkState = MediatorLiveData<NetworkState>()
    private var clearOld: (() -> Unit)? = null

    fun invalidateItems(userType: UserType) {
        clearOld?.invoke()
        val result = subscribersRepository.getSubscribers(
            viewModelScope,
            reversed = userType == UserType.Subscription
        )
        clearOld = _users.pickFrom(result.list)
        networkState.pickFrom(result.state)
    }
}
