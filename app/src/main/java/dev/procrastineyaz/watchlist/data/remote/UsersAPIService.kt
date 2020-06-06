package dev.procrastineyaz.watchlist.data.remote

import dev.procrastineyaz.watchlist.data.remote.dto.LoginResponse
import dev.procrastineyaz.watchlist.data.remote.dto.RemoteUser
import dev.procrastineyaz.watchlist.data.remote.dto.UserCredentials
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsersAPIService {
    @POST("users/login")
    suspend fun login(@Body credentials: UserCredentials): LoginResponse

    @GET("users/self")
    suspend fun getSelf(): RemoteUser
}
