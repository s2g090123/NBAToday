package com.jiachian.nbatoday.home.user.domain.error

import com.jiachian.nbatoday.common.domain.Error

enum class UserRegisterError : Error {
    INVALID_ACCOUNT,
    REGISTER_FAILED,
}
