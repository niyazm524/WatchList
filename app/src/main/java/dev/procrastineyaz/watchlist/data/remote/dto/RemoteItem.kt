package dev.procrastineyaz.watchlist.data.remote.dto

import java.util.*

data class RemoteItem(
    val id: Long,
    val nameRu: String,
    val nameEn: String,
    val description: String,
    val year: String?,
    val categoryId: Int,
    val createdAt: Date?,
    val rating: String,
    val userRating: Float?,
    val note: String?,
    val seen: Boolean?,
    val posterUrl: String?
)

data class ItemsQueryDto(
    val category: String? = null,
    val filter: String? = null,
    val search: String? = null,
    val page: Int? = null,
    val itemsPerPage: Int? = null,
)

data class ItemsSearchResponseDto(
    val keywords: String,
    val pagesCount: Int,
    val count: Int,
    val items: List<RemoteItem>
)

data class NewItemDto(
    val itemId: Int,
    val seen: Boolean = false,
    val rating: Int? = null,
    val note: String? = null
)

data class ItemsListResponse(
    override val items: List<RemoteItem>,
    override val count: Int,
    override val itemsPerPage: Int,
) : IPageableResponse<RemoteItem>

data class UserItemPropsDto(
    val rating: Float? = null,
    val note: String? = null,
    val seen: Boolean? = null,
)
