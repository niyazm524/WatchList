package dev.procrastineyaz.watchlist.ui.main.common

import androidx.lifecycle.LiveData
import androidx.navigation.Navigator
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.SeenParameter

interface ItemsAdapterProvider {
    fun getItemsLiveData(seen: SeenParameter): LiveData<PagedList<Item>>

    fun onItemClick(item: Item, extras: Navigator.Extras) {}
}
