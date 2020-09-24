package dev.procrastineyaz.watchlist.data.repositories.sources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.NetworkState

class ItemsSourceFactory(
    private val factory: () -> BaseItemsDataSource
) : DataSource.Factory<Int, Item>() {
    private val isLoadingMutable = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoadingMutable
    private val networkStateMutable = MutableLiveData<NetworkState>(NetworkState.Success)
    val networkState: LiveData<NetworkState> = networkStateMutable

    override fun create(): DataSource<Int, Item> {
        return factory().also { source ->
            source._isLoading = isLoadingMutable
            source._networkState = networkStateMutable
        }
    }
}
