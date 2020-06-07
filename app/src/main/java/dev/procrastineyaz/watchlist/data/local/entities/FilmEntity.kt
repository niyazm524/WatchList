package dev.procrastineyaz.watchlist.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val nameRu: String,
    val nameEn: String,
    val year: Int,
    val description: String,
    val rating: Float,
    val posterUrl: String,
    val categoryId: Int
)
