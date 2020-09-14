package dev.procrastineyaz.watchlist.data.remote.dto

import java.util.*

data class Item(
    val id: Long,
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

data class ItemsQueryDto (
    val category: String?,
    val filter: String?,
    val search: String?,
    val page: Int?,
    val itemsPerPage: Int?
)
