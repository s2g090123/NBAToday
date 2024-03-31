package com.jiachian.nbatoday.datasource.remote.data

import com.jiachian.nbatoday.data.remote.RemoteUserGenerator
import com.jiachian.nbatoday.datasource.remote.user.UserRemoteSource
import com.jiachian.nbatoday.home.user.data.model.remote.UserDto
import retrofit2.Response

class TestUserRemoteSource : UserRemoteSource() {
    override suspend fun login(account: String, password: String): Response<UserDto> {
        return Response.success(RemoteUserGenerator.get())
    }

    override suspend fun register(account: String, password: String): Response<UserDto> {
        return Response.success(RemoteUserGenerator.get())
    }

    override suspend fun updatePassword(account: String, password: String, token: String): Response<String> {
        return Response.success("")
    }

    override suspend fun updatePoints(account: String, points: Long, token: String): Response<String> {
        return Response.success("")
    }
}
