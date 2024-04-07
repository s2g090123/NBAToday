package com.jiachian.nbatoday.home.user.data.model

import com.jiachian.nbatoday.home.user.data.model.local.User
import com.jiachian.nbatoday.home.user.data.model.remote.UserDto
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero

fun UserDto.toUser(): User {
    val available = account != null && token != null
    return User(
        account = account.getOrNA(),
        name = name.getOrNA(),
        points = points.getOrZero(),
        password = password ?: "",
        token = token ?: "",
        available = available,
    )
}
