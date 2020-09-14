package dev.procrastineyaz.watchlist.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.SeenParameter
import dev.procrastineyaz.watchlist.ui.main.common.ItemsAdapterProvider

class HomeViewModel : ViewModel(), ItemsAdapterProvider {

    private var currentTab: SeenParameter = SeenParameter.SEEN
    private val _seenItems = MutableLiveData<List<Item>>(listOf())
    private val seenItems: LiveData<List<Item>> = _seenItems
    private val _unseenItems = MutableLiveData<List<Item>>(listOf())
    private val unseenItems: LiveData<List<Item>> = _unseenItems

    fun setCurrentTab(tab: SeenParameter) {
        currentTab = tab
    }

    override fun getItemsLiveData(seen: SeenParameter): LiveData<List<Item>> = when(seen) {
        SeenParameter.SEEN -> seenItems
        SeenParameter.UNSEEN -> unseenItems
        else -> seenItems
    }


}
