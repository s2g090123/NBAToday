package com.jiachian.nbatoday.common.ui.login.error

import com.jiachian.nbatoday.home.user.domain.error.UserLoginError
import com.jiachian.nbatoday.home.user.domain.error.UserRegisterError

enum class LoginDialogError(val message: String) {
    INVALID_ACCOUNT("Account is invalid."),
    LOGIN_FAILED("Login failed."),
}

fun UserLoginError.asLoginDialogError(): LoginDialogError {
    return when (this) {
        UserLoginError.INVALID_ACCOUNT -> LoginDialogError.INVALID_ACCOUNT
        UserLoginError.LOGIN_FAILED -> LoginDialogError.LOGIN_FAILED
    }
}

fun UserRegisterError.asLoginDialogError(): LoginDialogError {
    return when (this) {
        UserRegisterError.INVALID_ACCOUNT -> LoginDialogError.INVALID_ACCOUNT
        UserRegisterError.REGISTER_FAILED -> LoginDialogError.LOGIN_FAILED
    }
}
