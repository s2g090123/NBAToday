package com.jiachian.nbatoday.compose.screen.account.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.compose.screen.account.event.LoginDataEvent

@Stable
interface LoginState {
    val account: String
    val password: String
    val valid: Boolean
    val event: LoginDataEvent?
}

class MutableLoginState : LoginState {
    override var account: String by mutableStateOf("")
    override var password: String by mutableStateOf("")
    override val valid: Boolean by derivedStateOf { account.isNotBlank() && password.isNotBlank() }
    override var event: LoginDataEvent? by mutableStateOf(null)
}
