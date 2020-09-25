package dev.procrastineyaz.watchlist.data.repositories.sources

import dev.procrastineyaz.watchlist.data.dto.SubscribeUser
import dev.procrastineyaz.watchlist.data.remote.SubscribersAPIService
import dev.procrastineyaz.watchlist.data.remote.dto.IPageableResponse
import kotlinx.coroutines.CoroutineScope

class SubscriptionsDataSource(
    scope: CoroutineScope,
    private val subscribersAPIService: SubscribersAPIService,
    private val approved: Boolean? = null
) : BaseItemsDataSource<SubscribeUser>(scope) {

    override suspend fun fetchItems(
        page: Int,
        itemsPerPage: Int
    ): IPageableResponse<SubscribeUser> {
        return subscribersAPIService.getMySubscriptions(page, itemsPerPage, approved)
    }
}
