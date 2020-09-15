package dev.procrastineyaz.watchlist.data.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor


class MainThreadExecutor : Executor {
    private val handler: Handler = Handler(Looper.getMainLooper())
    override fun execute(r: Runnable) {
        handler.post(r)
    }
}
