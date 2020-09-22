package dev.procrastineyaz.watchlist.data.util
//
//import androidx.paging.DataSource
//import androidx.paging.PagedList
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import java.util.concurrent.Executor
//import java.util.concurrent.Executors
//
//class FlowPagedListBuilder<Key, Value>(
//    private val config: PagedList.Config,
//    private val dataSourceFactory: () -> DataSource<Key, Value>,
//) {
//    var initialKey: Key? = null
//    var boundaryCallback: PagedList.BoundaryCallback<Value>? = null
//    var notifyExecutor: Executor = MainThreadExecutor()
//    var fetchExecutor: Executor = Executors.newSingleThreadExecutor()
//
//    fun build(): Flow<PagedList<Value>> = flow {
//        var dataSource: DataSource<Key, Value>? = null
//        var list: PagedList<Value>? = null
//
//        val invalidatedCallback: DataSource.InvalidatedCallback = DataSource.InvalidatedCallback { dataSource?.invalidate() }
//
//        do {
//            dataSource?.removeInvalidatedCallback(invalidatedCallback)
//            dataSource = dataSourceFactory()
//            dataSource.addInvalidatedCallback(invalidatedCallback)
//            list = PagedList.Builder<Key, Value>(dataSource, config)
//                .setBoundaryCallback(boundaryCallback)
//                .setInitialKey(initialKey)
//                .setFetchExecutor(fetchExecutor)
//                .setNotifyExecutor(notifyExecutor)
//                .build()
//            emit(list)
//        } while (list?.isDetached != false)
//    }
//}
