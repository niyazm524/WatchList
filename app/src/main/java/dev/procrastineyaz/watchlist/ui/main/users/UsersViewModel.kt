package dev.procrastineyaz.watchlist.ui.main.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.NetworkState
import dev.procrastineyaz.watchlist.data.dto.Result
import dev.procrastineyaz.watchlist.data.dto.SubscribeUser
import dev.procrastineyaz.watchlist.data.dto.UserType
import dev.procrastineyaz.watchlist.data.repositories.SubscribersRepository
import dev.procrastineyaz.watchlist.ui.helpers.pickFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class UsersViewModel(private val subscribersRepository: SubscribersRepository) : ViewModel() {
    private var userType: UserType? = null
    private val _users = MediatorLiveData<PagedList<SubscribeUser>>()
    val users: LiveData<PagedList<SubscribeUser>> = _users
    val networkState = MediatorLiveData<NetworkState>()
    private var clearOld: (() -> Unit)? = null

    fun invalidateItems(userType: UserType? = this.userType) {
        if(userType != null) this.userType = userType
        else return
        clearOld?.invoke()
        val result = subscribersRepository.getSubscribers(
            viewModelScope,
            reversed = userType == UserType.Subscription
        )
        clearOld = _users.pickFrom(result.list)
        networkState.pickFrom(result.state)
    }

    fun onUserClicked(user: SubscribeUser) = viewModelScope.launch {
        if(user.isSubscriber && user.canSubscribe) {
            subscribersRepository.subscribe(user.id.toString())
                .flowOn(Dispatchers.IO)
                .collectLatest { invalidateItems() }
        }
    }

    fun onUserActionClicked(user: SubscribeUser) = viewModelScope.launch {
        if(user.isSubscriber) {
            if(!user.approved) executeFlow(subscribersRepository.approveSubscriber(user.id))
            else if(user.canSubscribe) executeFlow(subscribersRepository.subscribe(user.id.toString()))
        } else {
            if(user.approved && !user.subscribedBack)
                executeFlow(subscribersRepository.unsubscribe(user.id))
        }
    }

    private suspend fun executeFlow(flow: Flow<Result<Unit, Throwable>>) {
        flow.flowOn(Dispatchers.IO).collectLatest { invalidateItems() }
    }

    fun onAddUser(username: String) = viewModelScope.launch {
        subscribersRepository.subscribe(username)
            .flowOn(Dispatchers.IO)
            .collectLatest { invalidateItems() }
    }
}
