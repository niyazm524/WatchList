package dev.procrastineyaz.watchlist.data.local.entities

enum class Category(val value: Int) {
    UNKNOWN(0),
    FILM(1),
    SERIES(2);

    companion object {
        fun fromId(id: Int): Category = when(id) {
            0 -> FILM
            1 -> SERIES
            else -> UNKNOWN
        }
    }
}
