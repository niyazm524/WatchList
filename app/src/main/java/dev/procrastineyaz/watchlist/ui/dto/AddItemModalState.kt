package dev.procrastineyaz.watchlist.ui.dto

import dev.procrastineyaz.watchlist.data.dto.Category

sealed class AddItemModalState {
    object Closed: AddItemModalState()
    data class Opened(val category: Category, val seen: Boolean): AddItemModalState()
}
