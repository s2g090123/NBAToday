package com.jiachian.nbatoday.home.user.data.model.remote

data class UpdatePointsBody(
    val account: String,
    val token: String,
    val points: Long
)
