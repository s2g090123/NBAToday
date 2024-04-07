package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.RemoteUserGenerator
import com.jiachian.nbatoday.home.user.data.UserService
import com.jiachian.nbatoday.home.user.data.model.remote.LoginBody
import com.jiachian.nbatoday.home.user.data.model.remote.UpdatePointsBody
import com.jiachian.nbatoday.home.user.data.model.remote.UserDto
import retrofit2.Response

class TestUserService : UserService {
    override suspend fun login(loginBody: LoginBody): Response<UserDto> {
        return Response.success(RemoteUserGenerator.get())
    }

    override suspend fun register(loginBody: LoginBody): Response<UserDto> {
        return Response.success(RemoteUserGenerator.get())
    }

    override suspend fun updatePoints(updatePointBody: UpdatePointsBody): Response<String> {
        return Response.success("")
    }
}
