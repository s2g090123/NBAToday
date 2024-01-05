package com.jiachian.nbatoday.repository.user

import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class UserRepository : BaseRepository() {
    abstract val user: Flow<User?>

    abstract suspend fun login(account: String, password: String)
    abstract suspend fun logout()
    abstract suspend fun register(account: String, password: String)

    abstract suspend fun updatePoints(points: Long)
    abstract suspend fun addPoints(points: Long)
}
