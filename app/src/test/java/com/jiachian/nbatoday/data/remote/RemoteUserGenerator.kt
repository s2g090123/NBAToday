package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.UserToken
import com.jiachian.nbatoday.home.user.data.model.remote.UserDto

object RemoteUserGenerator {
    fun get(): UserDto {
        return UserDto(
            account = UserAccount,
            name = UserName,
            points = UserPoints,
            password = UserPassword,
            token = UserToken,
        )
    }
}
