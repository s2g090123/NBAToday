package com.jiachian.nbatoday.home.user.domain.error

import com.jiachian.nbatoday.common.domain.Error

enum class UserRegisterError : Error {
    ACCOUNT_EMPTY,
    PASSWORD_EMPTY,
    LOGIN_FAILED,
}
