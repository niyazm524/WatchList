package dev.procrastineyaz.watchlist.data.remote

import dev.procrastineyaz.watchlist.data.remote.dto.SubscribersResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SubscribersAPIService {

    @GET("subscriptions")
    suspend fun getMySubscribers(
        @Query("page") page: Int? = null,
        @Query("itemsPerPage") itemsPerPage: Int? = null,
        @Query("approved") approved: Boolean? = null,
    ): SubscribersResponse

    @GET("subscriptions/reverse")
    suspend fun getMySubscriptions(
        @Query("page") page: Int? = null,
        @Query("itemsPerPage") itemsPerPage: Int? = null,
        @Query("approved") approved: Boolean? = null,
    ): SubscribersResponse

    @POST("subscriptions/subscribe/{user}")
    suspend fun subscribe(@Path("user") user: String)

    @POST("subscriptions/unsubscribe/{userId}")
    suspend fun unsubscribe(@Path("userId") userId: Long)

    @POST("subscriptions/approve/{userId}")
    suspend fun approveSubscriber(@Path("userId") userId: Long)
}
