package dev.procrastineyaz.watchlist.data.repositories

import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.local.entities.ItemEntity

class ItemsBoundaryCallback : PagedList.BoundaryCallback<ItemEntity>() {
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
    }

    override fun onItemAtEndLoaded(itemAtEnd: ItemEntity) {
        super.onItemAtEndLoaded(itemAtEnd)
    }
}
