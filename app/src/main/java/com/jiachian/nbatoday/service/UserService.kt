package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.user.LoginBody
import com.jiachian.nbatoday.data.remote.user.UpdatePasswordBody
import com.jiachian.nbatoday.data.remote.user.UpdatePointBody
import com.jiachian.nbatoday.data.remote.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("user/login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): Response<User>

    @POST("user/register")
    suspend fun register(
        @Body loginBody: LoginBody
    ): Response<User>

    @POST("user/password")
    suspend fun updatePassword(
        @Body updatePasswordBody: UpdatePasswordBody
    ): Response<String>

    @POST("user/points")
    suspend fun updatePoints(
        @Body updatePointBody: UpdatePointBody
    ): Response<String>
}
