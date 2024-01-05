package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.UserToken
import com.jiachian.nbatoday.models.local.user.User

object UserGenerator {
    fun get(available: Boolean): User {
        return User(
            account = UserAccount,
            name = UserName,
            points = UserPoints,
            password = UserPassword,
            token = UserToken,
            available = available
        )
    }
}
