package com.jiachian.nbatoday.datasource.remote.user

import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.models.remote.user.RemoteUser

abstract class UserRemoteSource : RemoteSource() {
    abstract suspend fun login(account: String, password: String): RemoteUser?
    abstract suspend fun register(account: String, password: String): RemoteUser?
    abstract suspend fun updatePassword(account: String, password: String, token: String): String?
    abstract suspend fun updatePoints(account: String, points: Long, token: String): String?
}
