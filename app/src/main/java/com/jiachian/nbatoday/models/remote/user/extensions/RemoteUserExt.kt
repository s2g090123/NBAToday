package com.jiachian.nbatoday.models.remote.user.extensions

import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.models.remote.user.RemoteUser
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero

fun RemoteUser.toUser(): User {
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
