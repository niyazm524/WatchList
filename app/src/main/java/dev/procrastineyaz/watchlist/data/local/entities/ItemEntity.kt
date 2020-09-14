package dev.procrastineyaz.watchlist.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "items", indices = [Index(value = ["seen"], name = "seen_index")])
data class ItemEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val nameRu: String,
    val nameEn: String,
    val description: String,
    val year: Int?,
    val categoryId: Int,
    val createdAt: Date,
    val rating: Float,
    val userRating: Float?,
    val note: String?,
    val seen: Boolean,
    val posterUrl: String?
)
