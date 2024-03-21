package com.jiachian.nbatoday.compose.screen.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.compose.screen.account.event.LoginDataEvent
import com.jiachian.nbatoday.compose.screen.account.event.LoginUIEvent
import com.jiachian.nbatoday.compose.screen.account.state.LoginState
import com.jiachian.nbatoday.compose.screen.account.state.MutableLoginState
import com.jiachian.nbatoday.usecase.user.UserUseCase
import kotlinx.coroutines.launch

class LoginDialogViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val stateImp = MutableLoginState()
    val state: LoginState = stateImp

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            LoginUIEvent.Login -> login()
            LoginUIEvent.Register -> register()
            is LoginUIEvent.TextAccount -> stateImp.account = event.account
            is LoginUIEvent.TextPassword -> stateImp.password = event.password
            LoginUIEvent.EventReceived -> stateImp.event = null
        }
    }

    private fun login() {
        viewModelScope.launch {
            when (val resource = userUseCase.userLogin(state.account, state.password)) {
                is Resource.Loading -> Unit
                is Resource.Error -> stateImp.event = LoginDataEvent.Error(resource.error.asLoginDialogError())
                is Resource.Success -> stateImp.event = LoginDataEvent.Login
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            when (val resource = userUseCase.userRegister(state.account, state.password)) {
                is Resource.Loading -> Unit
                is Resource.Error -> stateImp.event = LoginDataEvent.Error(resource.error.asLoginDialogError())
                is Resource.Success -> stateImp.event = LoginDataEvent.Login
            }
        }
    }
}
