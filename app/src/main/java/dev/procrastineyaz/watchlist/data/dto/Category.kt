package dev.procrastineyaz.watchlist.data.dto

import androidx.annotation.Keep

@Keep enum class Category(val value: Int) {
    UNKNOWN(-1),
    FILM(0),
    SERIES(1),
    ANIME(2);

    fun toStringBackend(): String = when(value) {
        0 -> "film"
        1 -> "series"
        2 -> "anime"
        else -> "all"
    }

    companion object {
        fun fromId(id: Int): Category = when(id) {
            0 -> FILM
            1 -> SERIES
            2 -> ANIME
            else -> UNKNOWN
        }
    }
}
