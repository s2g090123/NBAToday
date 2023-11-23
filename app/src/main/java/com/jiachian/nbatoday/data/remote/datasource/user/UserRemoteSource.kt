package com.jiachian.nbatoday.data.remote.datasource.user

import com.jiachian.nbatoday.data.remote.datasource.RemoteSource
import com.jiachian.nbatoday.data.remote.user.User

abstract class UserRemoteSource : RemoteSource() {
    abstract suspend fun login(account: String, password: String): User?
    abstract suspend fun register(account: String, password: String): User?
    abstract suspend fun updatePassword(account: String, password: String, token: String): String?
    abstract suspend fun updatePoints(account: String, points: Long, token: String): String?
}
