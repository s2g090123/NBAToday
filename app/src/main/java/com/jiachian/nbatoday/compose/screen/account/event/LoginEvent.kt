package com.jiachian.nbatoday.compose.screen.account.event

sealed class LoginEvent {
    class TextAccount(val account: String) : LoginEvent()
    class TextPassword(val password: String) : LoginEvent()
    object Login : LoginEvent()
    object Register : LoginEvent()
    object ErrorSeen : LoginEvent()
}
