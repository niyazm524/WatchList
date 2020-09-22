package dev.procrastineyaz.watchlist.data.remote

import dev.procrastineyaz.watchlist.data.remote.dto.ItemsListResponse
import dev.procrastineyaz.watchlist.data.remote.dto.ItemsSearchResponseDto
import dev.procrastineyaz.watchlist.data.remote.dto.NewItemDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ItemsAPIService {

    @GET("items")
    suspend fun getItems(
        @Query("category") category: String? = null,
        @Query("filter") filter: String? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("itemsPerPage") itemsPerPage: Int? = null,
    ): ItemsListResponse

    @POST("items")
    suspend fun addItem(@Body newItem: NewItemDto)

    @GET("kinopoisk/search")
    suspend fun searchItems(
        @Query("keywords") query: String,
        @Query("page") page: Int = 1
    ): ItemsSearchResponseDto
}
