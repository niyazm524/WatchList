package dev.procrastineyaz.watchlist.data.remote.dto

import java.io.IOException

class ServerError(message: String, val code: Int) : IOException(message)
