package dev.procrastineyaz.watchlist.data.remote

import dev.procrastineyaz.watchlist.data.remote.dto.FeedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedAPIService {
    @GET("feed")
    suspend fun fetchFeed(
        @Query("page") page: Int? = null,
        @Query("itemsPerPage") itemsPerPage: Int? = null,
    ): FeedResponse
}
