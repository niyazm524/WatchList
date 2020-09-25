package dev.procrastineyaz.watchlist.data.repositories.sources

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import dev.procrastineyaz.watchlist.data.dto.NetworkState
import dev.procrastineyaz.watchlist.data.remote.dto.IPageableResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseItemsDataSource<V>(
    protected val scope: CoroutineScope
) : PageKeyedDataSource<Int, V>() {
    var _isLoading: MutableLiveData<Boolean>? = null
    var _networkState: MutableLiveData<NetworkState>? = null
    protected var inProcess: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            _isLoading?.postValue(value)
        }


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, V>
    ) {
        if (inProcess) {
            return
        }
        scope.launch {
            inProcess = true
            try {
                val response =
                    withContext(Dispatchers.IO) { fetchItems(1, params.requestedLoadSize) }
                _networkState?.postValue(NetworkState.Success)
                callback.onResult(
                    response.getMappedItems(),
                    0,
                    response.count,
                    null,
                    if (response.pagesCount() > 1) 2 else null
                )
            } catch (err: Throwable) {
                _networkState?.postValue(NetworkState.Failure(err))
            } finally {
                inProcess = false
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, V>) {
        val key: Int? = params.key
        if (inProcess || key == null) {
            return
        }

        inProcess = true
        scope.launch {
            try {
                val response =
                    withContext(Dispatchers.IO) { fetchItems(key, params.requestedLoadSize) }
                _networkState?.postValue(NetworkState.Success)
                callback.onResult(
                    response.getMappedItems(),
                    if (response.pagesCount() > key) key + 1 else null
                )
            } catch (err: Throwable) {
                _networkState?.postValue(NetworkState.Failure(err))
            } finally {
                inProcess = false
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, V>) {}

    abstract suspend fun fetchItems(page: Int, itemsPerPage: Int): IPageableResponse<V>
}
