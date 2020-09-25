package dev.procrastineyaz.watchlist.ui.main.home

import androidx.lifecycle.*
import androidx.navigation.Navigator
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.ItemsQuery
import dev.procrastineyaz.watchlist.data.dto.SeenParameter
import dev.procrastineyaz.watchlist.data.repositories.ItemsRepository
import dev.procrastineyaz.watchlist.ui.dto.AddItemModalState
import dev.procrastineyaz.watchlist.ui.dto.ItemsProviders
import dev.procrastineyaz.watchlist.ui.main.common.ItemsAdapterProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val itemsRepository: ItemsRepository,
    private val itemProvider: ItemsProviders
) : ViewModel(),
    ItemsAdapterProvider {

    private var currentTab: SeenParameter = SeenParameter.SEEN
    private val _seenItems = MutableLiveData<PagedList<Item>>()
    private val _unseenItems = MutableLiveData<PagedList<Item>>()
    private val _addItemModalState = MutableLiveData<AddItemModalState>(AddItemModalState.Closed)
    val addItemModalState: LiveData<AddItemModalState> = _addItemModalState
    private val _itemForDetails = MutableLiveData<Pair<Item, Navigator.Extras>?>(null)
    val itemForDetails: LiveData<Pair<Item, Navigator.Extras>?> = _itemForDetails
    private val searchPhrase: String? = null
    private var category: Category = Category.UNKNOWN
    private var fetchingJob: Job? = null
    var subscriptionUserId: Long? = null

    fun setCurrentTab(tab: SeenParameter) {
        currentTab = tab
    }

    private fun getCurrentTabLiveData(seen: SeenParameter = currentTab) =
        if (seen == SeenParameter.SEEN) _seenItems else _unseenItems

    override fun getItemsLiveData(seen: SeenParameter): LiveData<PagedList<Item>> {
        return getCurrentTabLiveData(seen)
    }

    private fun invalidateItems() {
        fetchingJob?.cancel("Running another fetch session")
        fetchingJob = viewModelScope.launch {
            val seen = async { invalidateList(seen = SeenParameter.SEEN, _seenItems) }
            val unseen = async { invalidateList(seen = SeenParameter.UNSEEN, _unseenItems) }
            seen.await()
            unseen.await()
        }
    }

    private suspend fun invalidateList(
        seen: SeenParameter,
        toLiveData: MutableLiveData<PagedList<Item>>
    ) {
        val query = ItemsQuery(category, seen, searchPhrase)
        val subId = subscriptionUserId
        val liveData: LiveData<PagedList<Item>> = when {
            itemProvider == ItemsProviders.HomeItemsVM -> itemsRepository.getItems(query, viewModelScope)
            subId != null -> itemsRepository.getSubscriptionItems(subId, query, viewModelScope)
            else -> return
        }
        liveData
            .asFlow()
            .flowOn(Dispatchers.IO)
            .collectLatest {
                toLiveData.postValue(it)
            }
    }

    fun selectCategory(category: Category) {
        this.category = category
        invalidateItems()
    }

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

    override fun onItemClick(item: Item, extras: Navigator.Extras) {
        _itemForDetails.value = item to extras
    }

    fun navigationToItemCompleted() {
        _itemForDetails.value = null
    }


}
