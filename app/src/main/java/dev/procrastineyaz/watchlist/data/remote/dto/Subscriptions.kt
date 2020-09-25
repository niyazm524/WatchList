package dev.procrastineyaz.watchlist.data.remote.dto

import dev.procrastineyaz.watchlist.data.dto.SubscribeUser

data class SubscribersResponse(
    override val count: Int,
    override val itemsPerPage: Int,
    override val items: List<SubscribeUser>
) : IPageableResponse<SubscribeUser>
