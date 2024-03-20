package com.jiachian.nbatoday.compose.screen.account.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
interface LoginState {
    val account: String
    val password: String
    val isLogin: Boolean
    val error: String?
}

class MutableLoginState : LoginState {
    override var account: String by mutableStateOf("")
    override var password: String by mutableStateOf("")
    override var isLogin: Boolean by mutableStateOf(false)
    override var error: String? by mutableStateOf(null)
}
