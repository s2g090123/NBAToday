package com.jiachian.nbatoday.home.user.data.model.local

data class User(
    val account: String,
    val name: String,
    val points: Long,
    val password: String,
    val token: String,
    val available: Boolean,
)
