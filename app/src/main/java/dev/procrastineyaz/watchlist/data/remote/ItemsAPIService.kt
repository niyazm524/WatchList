package dev.procrastineyaz.watchlist.data.remote

import dev.procrastineyaz.watchlist.data.remote.dto.ItemsListResponse
import dev.procrastineyaz.watchlist.data.remote.dto.ItemsSearchResponseDto
import dev.procrastineyaz.watchlist.data.remote.dto.NewItemDto
import dev.procrastineyaz.watchlist.data.remote.dto.UserItemPropsDto
import retrofit2.http.*

interface ItemsAPIService {

    @GET("items")
    suspend fun getItems(
        @Query("category") category: String? = null,
        @Query("filter") filter: String? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("itemsPerPage") itemsPerPage: Int? = null,
    ): ItemsListResponse

    @GET("subscriptions/{userId}/items")
    suspend fun getSubscriptionItems(
        @Path("userId") userId: Long,
        @Query("category") category: String? = null,
        @Query("filter") filter: String? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("itemsPerPage") itemsPerPage: Int? = null,
    ): ItemsListResponse

    @POST("items")
    suspend fun addItem(@Body newItem: NewItemDto)

    @PUT("items/{id}")
    suspend fun updateItem(
        @Path("id") id: Int,
        @Body userItemProps: UserItemPropsDto
    ): Boolean

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: Int)

    @GET("kinopoisk/search")
    suspend fun searchItems(
        @Query("keywords") query: String,
        @Query("page") page: Int = 1
    ): ItemsSearchResponseDto
}
