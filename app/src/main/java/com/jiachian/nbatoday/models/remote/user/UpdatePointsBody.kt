package com.jiachian.nbatoday.models.remote.user

data class UpdatePointsBody(
    val account: String,
    val token: String,
    val points: Long
)
