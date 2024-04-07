package com.jiachian.nbatoday.home.user.data

import com.jiachian.nbatoday.home.user.data.model.remote.LoginBody
import com.jiachian.nbatoday.home.user.data.model.remote.UpdatePointsBody
import com.jiachian.nbatoday.home.user.data.model.remote.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("user/login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): Response<UserDto>

    @POST("user/register")
    suspend fun register(
        @Body loginBody: LoginBody
    ): Response<UserDto>

    @POST("user/points")
    suspend fun updatePoints(
        @Body updatePointBody: UpdatePointsBody
    ): Response<String>
}
