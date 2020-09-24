package dev.procrastineyaz.watchlist.ui.main.trends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.NetworkState
import dev.procrastineyaz.watchlist.data.repositories.TrendsRepository
import dev.procrastineyaz.watchlist.ui.helpers.pickFrom

class TrendsViewModel(private val trendsRepository: TrendsRepository) : ViewModel() {
    private val _items = MediatorLiveData<PagedList<Item>>()
    val items: LiveData<PagedList<Item>> = _items
    val networkState = MediatorLiveData<NetworkState>()
    private var clearOld: (() -> Unit)? = null

    fun selectCategory(category: Category) {
        return invalidateItems(category)
    }

    private fun invalidateItems(category: Category) {
        clearOld?.invoke()
        val result = trendsRepository.getTrends(viewModelScope, category)
        clearOld = _items.pickFrom(result.list)
        networkState.pickFrom(result.state)
    }

}
