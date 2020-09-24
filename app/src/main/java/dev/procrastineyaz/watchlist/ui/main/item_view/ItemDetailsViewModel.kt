package dev.procrastineyaz.watchlist.ui.main.item_view

import androidx.lifecycle.*
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.Result
import dev.procrastineyaz.watchlist.data.repositories.ItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias ItemState = Triple<String?, Float?, Boolean?>

class ItemDetailsViewModel(private val itemsRepository: ItemsRepository): ViewModel() {
    private lateinit var item: Item
    val note = MutableLiveData<String?>()
    val userRating = MutableLiveData<Float?>()
    val seen = MutableLiveData<Boolean>()
    val state = MediatorLiveData<ItemState>()
    private var lastUpdatedState: ItemState? = null
    private val _updateResult = MutableLiveData<Result<Boolean, Throwable>?>(null)
    val updateResult: LiveData<Result<Boolean, Throwable>?> = _updateResult
    private val _deleteResult = MutableLiveData<Result<Unit, Throwable>?>(null)
    val deleteResult: LiveData<Result<Unit, Throwable>?> = _deleteResult

    private val stateObserver = Observer<Any?> { _ ->
        state.postValue(ItemState(
            if(note.value?.isNotEmpty() == true) note.value else null,
            userRating.value,
            seen.value
        ))
    }

    init {
        state.addSource(note, stateObserver)
        state.addSource(userRating, stateObserver)
        state.addSource(seen, stateObserver)
        observeItemChanges(state.asFlow())
    }

    fun setItem(item: Item) {
        this.item = item
        note.value = item.note
        userRating.value = item.userRating
        seen.value = item.seen
    }

    private fun observeItemChanges(state: Flow<ItemState>) = viewModelScope.launch {
        state
            .debounce(2000)
            .drop(1)
            .flowOn(Dispatchers.Main)
            .flatMapLatest { state ->
                itemsRepository.updateItem(item.id.toInt(), state.third, state.second, state.first)
            }
            .flowOn(Dispatchers.IO)
            .collectLatest {
                lastUpdatedState = this@ItemDetailsViewModel.state.value
                _updateResult.postValue(it)
            }
    }

    fun onSave() {
        if(state.value != null && state.value != lastUpdatedState) {
            flowOf(state.value)
                .filterNotNull()
                .flatMapLatest { state ->
                    itemsRepository.updateItem(item.id.toInt(), state.third, state.second, state.first)
                }
                .flowOn(Dispatchers.IO)
                .launchIn(GlobalScope)
        }
    }

    fun deleteItem() = GlobalScope.launch {
        itemsRepository.deleteItem(item.id.toInt())
            .flowOn(Dispatchers.IO)
            .collectLatest {
                withContext(Dispatchers.Main) {
                    _deleteResult.value = it
                    _deleteResult.value = null
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        state.removeObserver(stateObserver)
        state.removeSource(note)
        state.removeSource(userRating)
        state.removeSource(seen)
    }

}
