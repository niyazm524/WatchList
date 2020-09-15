package dev.procrastineyaz.watchlist.data.local.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
data class ItemEntity(
    @Id var id: Long = 0,
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
