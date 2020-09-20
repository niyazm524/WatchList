package dev.procrastineyaz.watchlist.ui.helpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> MediatorLiveData<T>.pickFrom(liveData: LiveData<T>) {
    addSource(liveData) { value ->
        this.postValue(value)
    }
}
