package com.jiachian.nbatoday.datasource.remote.user

import com.jiachian.nbatoday.models.remote.user.LoginBody
import com.jiachian.nbatoday.models.remote.user.RemoteUser
import com.jiachian.nbatoday.models.remote.user.UpdatePasswordBody
import com.jiachian.nbatoday.models.remote.user.UpdatePointBody
import com.jiachian.nbatoday.service.UserService

class NbaUserRemoteSource : UserRemoteSource() {

    private val userService by lazy {
        retrofit.create(UserService::class.java)
    }

    override suspend fun login(account: String, password: String): RemoteUser? {
        return userService.login(LoginBody(account, password)).body()
    }

    override suspend fun register(account: String, password: String): RemoteUser? {
        return userService.register(LoginBody(account, password)).body()
    }

    override suspend fun updatePassword(account: String, password: String, token: String): String? {
        return userService.updatePassword(UpdatePasswordBody(account, token, password)).body()
    }

    override suspend fun updatePoints(account: String, points: Long, token: String): String? {
        return userService.updatePoints(UpdatePointBody(account, token, points)).body()
    }
}
