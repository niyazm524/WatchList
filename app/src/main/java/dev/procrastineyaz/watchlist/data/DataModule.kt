package dev.procrastineyaz.watchlist.data

import dev.procrastineyaz.watchlist.data.local.localDBModule
import dev.procrastineyaz.watchlist.data.remote.remoteAPIModule


val dataModule = localDBModule + remoteAPIModule
