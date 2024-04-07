package com.jiachian.nbatoday.common.ui.login.event

sealed class LoginUIEvent {
    class TextAccount(val account: String) : LoginUIEvent()
    class TextPassword(val password: String) : LoginUIEvent()
    object Login : LoginUIEvent()
    object Register : LoginUIEvent()
    object EventReceived : LoginUIEvent()
}
