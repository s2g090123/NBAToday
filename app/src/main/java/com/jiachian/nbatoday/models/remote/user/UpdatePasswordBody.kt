package com.jiachian.nbatoday.models.remote.user

data class UpdatePasswordBody(
    val account: String,
    val token: String,
    val newPassword: String
)
