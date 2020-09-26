package dev.procrastineyaz.watchlist.ui.main.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.FeedItem
import dev.procrastineyaz.watchlist.data.dto.NetworkState
import dev.procrastineyaz.watchlist.data.repositories.FeedRepository
import dev.procrastineyaz.watchlist.ui.helpers.pickFrom

class FeedViewModel(private val feedRepository: FeedRepository) : ViewModel() {

    private val _feedItems = MediatorLiveData<PagedList<FeedItem>>()
    val feedItems: LiveData<PagedList<FeedItem>> = _feedItems
    private val _networkState = MediatorLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState
    private var clear: (() -> Unit)? = null

    fun invalidate() {
        clear?.invoke()
        val (list, isLoading, state) = feedRepository.getSubscribers(viewModelScope)
        clear = _feedItems.pickFrom(list)
        _networkState.pickFrom(state)
    }

    override fun onCleared() {
        clear?.invoke()
        super.onCleared()
    }

}
