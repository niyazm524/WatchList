package dev.procrastineyaz.watchlist.ui.main.home

import androidx.lifecycle.*
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.ItemsQuery
import dev.procrastineyaz.watchlist.data.dto.SeenParameter
import dev.procrastineyaz.watchlist.data.repositories.ItemsRepository
import dev.procrastineyaz.watchlist.ui.dto.AddItemModalState
import dev.procrastineyaz.watchlist.ui.main.common.ItemsAdapterProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(private val itemsRepository: ItemsRepository) : ViewModel(),
    ItemsAdapterProvider {

    private var currentTab: SeenParameter = SeenParameter.SEEN
    private val _seenItems = MutableLiveData<PagedList<Item>>()
    private val _unseenItems = MutableLiveData<PagedList<Item>>()
    private val _addItemModalState = MutableLiveData<AddItemModalState>(AddItemModalState.Closed)
    val addItemModalState: LiveData<AddItemModalState> = _addItemModalState
    private val searchPhrase: String? = null
    private var category: Category = Category.UNKNOWN

    fun setCurrentTab(tab: SeenParameter) {
        currentTab = tab
    }

    private fun getCurrentTabLiveData() =
        if (currentTab == SeenParameter.SEEN) _seenItems else _unseenItems

    override fun getItemsLiveData(seen: SeenParameter): LiveData<PagedList<Item>> {
        return getCurrentTabLiveData()
    }

    private fun invalidateItems() = viewModelScope.launch {
        val seen = async { invalidateList(seen = SeenParameter.SEEN, _seenItems) }
        val unseen = async { invalidateList(seen = SeenParameter.UNSEEN, _unseenItems) }
        seen.await()
        unseen.await()
    }

    private suspend fun invalidateList(
        seen: SeenParameter,
        toLiveData: MutableLiveData<PagedList<Item>>
    ) {
        itemsRepository.getItems(ItemsQuery(category, seen, searchPhrase), viewModelScope)
            .asFlow()
            .flowOn(Dispatchers.IO)
            .collect {
                toLiveData.postValue(it)
            }
    }

    fun selectCategory(category: Category) {
        this.category = category
        invalidateItems()
    }

    fun onAddNewItem() {
        _addItemModalState.postValue(
            AddItemModalState.Opened(
                category,
                currentTab == SeenParameter.SEEN
            )
        )
    }

    fun onNewItemAdded() {
        invalidateItems()
    }


}
