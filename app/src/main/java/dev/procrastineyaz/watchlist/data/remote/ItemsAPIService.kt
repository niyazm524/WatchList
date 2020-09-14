package dev.procrastineyaz.watchlist.data.remote

import dev.procrastineyaz.watchlist.data.remote.dto.Item
import dev.procrastineyaz.watchlist.data.remote.dto.ItemsQueryDto
import retrofit2.http.Body
import retrofit2.http.GET

interface ItemsAPIService {

    @GET("items")
    suspend fun getItems(@Body query: ItemsQueryDto) : List<Item>


}
