package com.jiachian.nbatoday.models.local.user

data class User(
    val account: String,
    val name: String,
    val points: Long,
    val password: String,
    val token: String,
    val isAvailable: Boolean,
)
