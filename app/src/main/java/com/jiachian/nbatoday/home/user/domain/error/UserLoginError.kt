package com.jiachian.nbatoday.home.user.domain.error

import com.jiachian.nbatoday.common.domain.Error

enum class UserLoginError : Error {
    INVALID_ACCOUNT,
    LOGIN_FAILED,
}
