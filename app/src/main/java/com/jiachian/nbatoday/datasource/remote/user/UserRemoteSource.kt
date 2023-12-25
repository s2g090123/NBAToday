package com.jiachian.nbatoday.datasource.remote.user

import com.jiachian.nbatoday.models.remote.user.RemoteUser
import retrofit2.Response

abstract class UserRemoteSource {
    abstract suspend fun login(account: String, password: String): Response<RemoteUser>
    abstract suspend fun register(account: String, password: String): Response<RemoteUser>
    abstract suspend fun updatePassword(account: String, password: String, token: String): Response<String>
    abstract suspend fun updatePoints(account: String, points: Long, token: String): Response<String>
}
