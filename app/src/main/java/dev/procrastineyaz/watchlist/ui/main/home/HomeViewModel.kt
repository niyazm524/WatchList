package dev.procrastineyaz.watchlist.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.procrastineyaz.watchlist.data.dto.SeenParameter
import dev.procrastineyaz.watchlist.data.repositories.ItemsRepository
import dev.procrastineyaz.watchlist.ui.dto.AddItemModalState
import dev.procrastineyaz.watchlist.ui.main.common.BaseItemsViewModel
import dev.procrastineyaz.watchlist.ui.main.common.ItemsAdapterProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeViewModel(itemsRepository: ItemsRepository, ) : BaseItemsViewModel(itemsRepository),
    ItemsAdapterProvider {
    private val _addItemModalState = MutableLiveData<AddItemModalState>(AddItemModalState.Closed)
    val addItemModalState: LiveData<AddItemModalState> = _addItemModalState

    fun onAddNewItem() {
        _addItemModalState.value = AddItemModalState.Opened(
            category,
            currentTab == SeenParameter.SEEN
        )
        _addItemModalState.value = AddItemModalState.Closed
    }

    fun onNewItemAdded() {
        invalidateItems()
    }
}
