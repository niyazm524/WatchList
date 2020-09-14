package dev.procrastineyaz.watchlist.data.dto

enum class Category(val value: Int) {
    UNKNOWN(-1),
    FILM(0),
    SERIES(1),
    ANIME(2);

    companion object {
        fun fromId(id: Int): Category = when(id) {
            0 -> FILM
            1 -> SERIES
            2 -> ANIME
            else -> UNKNOWN
        }
    }
}
