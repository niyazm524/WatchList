package dev.procrastineyaz.watchlist.data.mappers

import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.local.entities.ItemEntity

fun ItemEntity.toItem() = Item(
    id = this.id,
    nameRu = this.nameRu,
    nameEn = this.nameEn,
    description = this.description,
    year = this.year,
    categoryId = this.categoryId,
    createdAt = this.createdAt,
    rating = this.rating,
    userRating = this.userRating,
    note = this.note,
    seen = this.seen,
    posterUrl = this.posterUrl,
)
