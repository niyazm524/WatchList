package dev.procrastineyaz.watchlist.ui.main.common

import androidx.lifecycle.*
import androidx.navigation.Navigator
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.ItemsQuery
import dev.procrastineyaz.watchlist.data.dto.SeenParameter
import dev.procrastineyaz.watchlist.data.repositories.ItemsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
open class BaseItemsViewModel(
    protected val itemsRepository: ItemsRepository,
) : ViewModel(),
    ItemsAdapterProvider {

    protected var currentTab: SeenParameter = SeenParameter.SEEN
    private val _seenItems = MutableLiveData<PagedList<Item>>()
    private val _unseenItems = MutableLiveData<PagedList<Item>>()
    protected val _itemForDetails = MutableLiveData<Pair<Item, Navigator.Extras>?>(null)
    val itemForDetails: LiveData<Pair<Item, Navigator.Extras>?> = _itemForDetails
    protected val searchPhrase: String? = null
    protected var category: Category = Category.UNKNOWN
    protected var fetchingJob: Job? = null


    private fun getCurrentTabLiveData(seen: SeenParameter = currentTab) =
        if (seen == SeenParameter.SEEN) _seenItems else _unseenItems

    override fun getItemsLiveData(seen: SeenParameter): LiveData<PagedList<Item>> {
        return getCurrentTabLiveData(seen)
    }

    protected fun invalidateItems() {
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
        getItemsFromRepo(query)
            .asFlow()
            .flowOn(Dispatchers.IO)
            .collectLatest {
                toLiveData.postValue(it)
            }
    }

    protected open fun getItemsFromRepo(query: ItemsQuery): LiveData<PagedList<Item>> {
        return itemsRepository.getItems(query, viewModelScope)
    }

    fun selectCategory(category: Category) {
        this.category = category
        invalidateItems()
    }

    fun selectCurrentTab(tab: SeenParameter) {
        currentTab = tab
    }

    override fun onItemClick(item: Item, extras: Navigator.Extras) {
        _itemForDetails.value = item to extras
        _itemForDetails.value = null
    }


}
