package com.jiachian.nbatoday.compose.screen.account.event

import com.jiachian.nbatoday.compose.screen.account.LoginDialogError

sealed class LoginDataEvent {
    object Login : LoginDataEvent()
    class Error(val error: LoginDialogError) : LoginDataEvent()
}
