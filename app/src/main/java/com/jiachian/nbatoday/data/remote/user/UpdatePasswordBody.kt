package com.jiachian.nbatoday.data.remote.user

data class UpdatePasswordBody(
    val account: String,
    val token: String,
    val newPassword: String
)
