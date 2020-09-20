package dev.procrastineyaz.watchlist.data.mappers

import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.local.entities.ItemEntity
import dev.procrastineyaz.watchlist.data.remote.dto.RemoteItem

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

fun RemoteItem.toItem() = Item(
    id = this.id,
    nameRu = this.nameRu,
    nameEn = this.nameEn,
    description = this.description,
    year = this.year,
    categoryId = this.categoryId,
    createdAt = this.createdAt,
    rating = (if(this.rating.isEmpty()) null else this.rating),
    userRating = this.userRating,
    note = this.note,
    seen = this.seen,
    posterUrl = this.posterUrl,
)

fun RemoteItem.toEntityItem(): ItemEntity {
    requireNotNull(this.createdAt)
    requireNotNull(this.seen)

    return ItemEntity(
        id = this.id,
        nameRu = this.nameRu,
        nameEn = this.nameEn,
        description = this.description,
        year = this.year,
        categoryId = this.categoryId,
        createdAt = this.createdAt,
        rating = (if(this.rating.isEmpty()) null else this.rating),
        userRating = this.userRating,
        note = this.note,
        seen = this.seen,
        posterUrl = this.posterUrl,
    )
}
