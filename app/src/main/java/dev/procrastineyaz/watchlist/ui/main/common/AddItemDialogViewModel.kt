package dev.procrastineyaz.watchlist.ui.main.common

import androidx.lifecycle.*
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.NetworkState
import dev.procrastineyaz.watchlist.data.repositories.ItemsRepository
import dev.procrastineyaz.watchlist.ui.helpers.pickFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class AddItemDialogViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {
    private val _liveItems = MediatorLiveData<PagedList<Item>>()
    val liveItems: LiveData<PagedList<Item>> = _liveItems
    private val _isLoading = MediatorLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _networkState = MediatorLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState

    fun onSearch(query: String, category: Category): Boolean {
        if (query.trim().isEmpty()) return false
        search(query, category)
        return true
    }

    private fun search(query: String, category: Category) {
        val (list, isLoading, state) = itemsRepository.searchItems(viewModelScope, query, category)
        _liveItems.pickFrom(list)
        _isLoading.pickFrom(isLoading)
        _networkState.pickFrom(state)
    }

    fun onItemClick(item: Item, seen: Boolean) = itemsRepository.addItem(item.id.toInt(), seen)
        .flowOn(Dispatchers.IO)
        .asLiveData()

}
