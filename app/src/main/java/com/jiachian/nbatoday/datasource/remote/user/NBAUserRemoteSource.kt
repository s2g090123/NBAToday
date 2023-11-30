package com.jiachian.nbatoday.datasource.remote.user

import com.jiachian.nbatoday.models.remote.user.LoginBody
import com.jiachian.nbatoday.models.remote.user.RemoteUser
import com.jiachian.nbatoday.models.remote.user.UpdatePasswordBody
import com.jiachian.nbatoday.models.remote.user.UpdatePointBody
import com.jiachian.nbatoday.service.UserService
import retrofit2.Response

class NBAUserRemoteSource : UserRemoteSource() {

    private val userService by lazy {
        retrofit.create(UserService::class.java)
    }

    override suspend fun login(account: String, password: String): Response<RemoteUser> {
        return userService.login(LoginBody(account, password))
    }

    override suspend fun register(account: String, password: String): Response<RemoteUser> {
        return userService.register(LoginBody(account, password))
    }

    override suspend fun updatePassword(account: String, password: String, token: String): Response<String> {
        return userService.updatePassword(UpdatePasswordBody(account, token, password))
    }

    override suspend fun updatePoints(account: String, points: Long, token: String): Response<String> {
        return userService.updatePoints(UpdatePointBody(account, token, points))
    }
}
