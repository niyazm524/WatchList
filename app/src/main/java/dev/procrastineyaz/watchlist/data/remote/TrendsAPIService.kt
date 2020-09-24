package dev.procrastineyaz.watchlist.data.remote

import dev.procrastineyaz.watchlist.data.remote.dto.TrendsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendsAPIService {
    @GET("trends")
    suspend fun getTrends(
        @Query("category") category: String? = null,
        @Query("page") page: Int? = null,
        @Query("itemsPerPage") itemsPerPage: Int? = null,
    ): TrendsResponseDto
}
