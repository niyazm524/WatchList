package dev.procrastineyaz.watchlist.data.repositories

import dev.procrastineyaz.watchlist.data.dto.LoginToken
import dev.procrastineyaz.watchlist.data.remote.UsersAPIService
import dev.procrastineyaz.watchlist.data.remote.dto.UserCredentials

class UsersRepository(
    private val usersAPIService: UsersAPIService
) {
    suspend fun login(username: String, password: String): LoginToken {
        val result = usersAPIService.login(UserCredentials(
            username = username,
            password = password
        ))
        return LoginToken(result.id, result.username, result.token)
    }
}
