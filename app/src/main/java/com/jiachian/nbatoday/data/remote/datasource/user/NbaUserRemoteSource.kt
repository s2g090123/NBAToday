package com.jiachian.nbatoday.data.remote.datasource.user

import com.jiachian.nbatoday.data.remote.user.LoginBody
import com.jiachian.nbatoday.data.remote.user.UpdatePasswordBody
import com.jiachian.nbatoday.data.remote.user.UpdatePointBody
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.service.ServiceProvider

class NbaUserRemoteSource : UserRemoteSource() {

    private val nbaService = ServiceProvider.nbaService

    override suspend fun login(account: String, password: String): User? {
        return nbaService.login(LoginBody(account, password)).body()
    }

    override suspend fun register(account: String, password: String): User? {
        return nbaService.register(LoginBody(account, password)).body()
    }

    override suspend fun updatePassword(account: String, password: String, token: String): String? {
        return nbaService.updatePassword(UpdatePasswordBody(account, token, password)).body()
    }

    override suspend fun updatePoints(account: String, points: Long, token: String): String? {
        return nbaService.updatePoints(UpdatePointBody(account, token, points)).body()
    }
}
