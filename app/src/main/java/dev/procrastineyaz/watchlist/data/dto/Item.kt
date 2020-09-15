package dev.procrastineyaz.watchlist.data.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
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
) : Parcelable

data class ItemsQuery(
    val category: Category,
    val filter: SeenParameter,
    val search: String?
)
