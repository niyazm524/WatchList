package dev.procrastineyaz.watchlist.data.dto

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class PagedItemsResult<T : Any>(
    val list: LiveData<PagedList<T>>,
    val isLoading: LiveData<Boolean>,
    val state: LiveData<NetworkState>,
)
