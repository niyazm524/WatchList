package dev.procrastineyaz.watchlist.data.repositories

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dev.procrastineyaz.watchlist.data.dto.PagedItemsResult
import dev.procrastineyaz.watchlist.data.dto.SubscribeUser
import dev.procrastineyaz.watchlist.data.remote.SubscribersAPIService
import dev.procrastineyaz.watchlist.data.repositories.sources.ItemsSourceFactory
import dev.procrastineyaz.watchlist.data.repositories.sources.SubscribersDataSource
import dev.procrastineyaz.watchlist.data.repositories.sources.SubscriptionsDataSource
import dev.procrastineyaz.watchlist.data.util.wrapInResultFlow
import kotlinx.coroutines.CoroutineScope

class SubscribersRepository(private val subscribersApi: SubscribersAPIService) {
    fun getSubscribers(
        scope: CoroutineScope,
        reversed: Boolean = false
    ): PagedItemsResult<SubscribeUser> {
        val factory =
            if (!reversed) ItemsSourceFactory { SubscribersDataSource(scope, subscribersApi) }
            else ItemsSourceFactory { SubscriptionsDataSource(scope, subscribersApi) }
        val pagedList = LivePagedListBuilder(
            factory,
            PagedList.Config.Builder()
                .setPageSize(20)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build()
        ).setInitialLoadKey(1).build()
        return PagedItemsResult(pagedList, factory.isLoading, factory.networkState)
    }

    fun subscribe(toUser: String) = wrapInResultFlow { subscribersApi.subscribe(toUser) }

    fun unsubscribe(fromUserId: Long) = wrapInResultFlow { subscribersApi.unsubscribe(fromUserId) }

    fun approveSubscriber(subId: Long) = wrapInResultFlow { subscribersApi.approveSubscriber(subId) }

}
