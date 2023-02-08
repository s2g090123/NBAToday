package com.jiachian.nbatoday.data.remote.user

data class UpdatePointBody(
    val account: String,
    val token: String,
    val points: Long
)