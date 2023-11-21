package com.jiachian.nbatoday.data.repository.user

import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.data.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class UserRepository : BaseRepository() {
    abstract val user: Flow<User?>

    abstract suspend fun login(account: String, password: String)
    abstract suspend fun logout()
    abstract suspend fun register(account: String, password: String)

    abstract suspend fun updatePassword(password: String)
    abstract suspend fun updatePoints(points: Long)
    abstract suspend fun addPoints(points: Long)
}
