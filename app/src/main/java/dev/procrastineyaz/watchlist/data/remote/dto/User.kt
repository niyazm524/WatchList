package dev.procrastineyaz.watchlist.data.remote.dto

data class RemoteUser(
    val id: String,
    val username: String,
    val email: String
)

data class UserCredentials(val username: String, val password: String)

data class NewUserDto(val username: String, val email: String, val password: String)
