package dev.procrastineyaz.watchlist

import android.app.Application
import dev.procrastineyaz.watchlist.data.dataModule
import dev.procrastineyaz.watchlist.services.serviceModules
import dev.procrastineyaz.watchlist.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WatchListApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WatchListApp)
            modules(uiModule + dataModule + serviceModules)
        }
    }
}
