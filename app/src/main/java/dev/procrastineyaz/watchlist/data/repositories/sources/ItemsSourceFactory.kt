package dev.procrastineyaz.watchlist.data.repositories.sources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import dev.procrastineyaz.watchlist.data.dto.NetworkState

class ItemsSourceFactory<T>(
    private val factory: () -> BaseItemsDataSource<T>
) : DataSource.Factory<Int, T>() {
    private val isLoadingMutable = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoadingMutable
    private val networkStateMutable = MutableLiveData<NetworkState>(NetworkState.Success)
    val networkState: LiveData<NetworkState> = networkStateMutable

    override fun create(): DataSource<Int, T> {
        return factory().also { source ->
            source._isLoading = isLoadingMutable
            source._networkState = networkStateMutable
        }
    }
}
