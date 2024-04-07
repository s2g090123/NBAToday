package com.jiachian.nbatoday.common.ui.login.event

import com.jiachian.nbatoday.common.ui.login.error.LoginDialogError

sealed class LoginDataEvent {
    object Login : LoginDataEvent()
    class Error(val error: LoginDialogError) : LoginDataEvent()
}
