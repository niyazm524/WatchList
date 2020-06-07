package dev.procrastineyaz.watchlist.data

import dev.procrastineyaz.watchlist.data.local.localDBModule
import dev.procrastineyaz.watchlist.data.remote.remoteAPIModule
import dev.procrastineyaz.watchlist.data.repositories.repositoriesModule


val dataModule = localDBModule + remoteAPIModule + repositoriesModule
