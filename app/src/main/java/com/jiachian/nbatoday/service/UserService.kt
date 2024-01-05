package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.models.remote.user.LoginBody
import com.jiachian.nbatoday.models.remote.user.RemoteUser
import com.jiachian.nbatoday.models.remote.user.UpdatePasswordBody
import com.jiachian.nbatoday.models.remote.user.UpdatePointsBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("user/login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): Response<RemoteUser>

    @POST("user/register")
    suspend fun register(
        @Body loginBody: LoginBody
    ): Response<RemoteUser>

    @POST("user/password")
    suspend fun updatePassword(
        @Body updatePasswordBody: UpdatePasswordBody
    ): Response<String>

    @POST("user/points")
    suspend fun updatePoints(
        @Body updatePointBody: UpdatePointsBody
    ): Response<String>
}
