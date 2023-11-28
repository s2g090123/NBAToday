package com.jiachian.nbatoday.models.remote.user

data class UpdatePointBody(
    val account: String,
    val token: String,
    val points: Long
)
