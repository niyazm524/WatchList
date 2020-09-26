package dev.procrastineyaz.watchlist.data.remote.dto

data class RemoteUser(
    val id: String,
    val username: String,
    val email: String,
    val subscribersCount: Int,
    val subscriptionsCount: Int,
)

data class UserPublicInfo(val id: String, val username: String, val posterUrl: String? = null)

data class UserCredentials(val username: String, val password: String)

data class NewUserDto(val username: String, val email: String, val password: String)
