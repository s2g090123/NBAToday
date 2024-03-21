package com.jiachian.nbatoday.compose.screen.account

import com.jiachian.nbatoday.usecase.user.UserLoginError
import com.jiachian.nbatoday.usecase.user.UserRegisterError

enum class LoginDialogError(val message: String) {
    ACCOUNT_EMPTY("Account is empty."),
    PASSWORD_EMPTY("Password is empty."),
    LOGIN_FAILED("Login failed."),
}

fun UserLoginError.asLoginDialogError(): LoginDialogError {
    return when (this) {
        UserLoginError.ACCOUNT_EMPTY -> LoginDialogError.ACCOUNT_EMPTY
        UserLoginError.PASSWORD_EMPTY -> LoginDialogError.PASSWORD_EMPTY
        UserLoginError.LOGIN_FAILED -> LoginDialogError.LOGIN_FAILED
    }
}

fun UserRegisterError.asLoginDialogError(): LoginDialogError {
    return when (this) {
        UserRegisterError.ACCOUNT_EMPTY -> LoginDialogError.ACCOUNT_EMPTY
        UserRegisterError.PASSWORD_EMPTY -> LoginDialogError.PASSWORD_EMPTY
        UserRegisterError.LOGIN_FAILED -> LoginDialogError.LOGIN_FAILED
    }
}
