package com.jiachian.nbatoday.common.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.common.ui.login.error.asLoginDialogError
import com.jiachian.nbatoday.common.ui.login.event.LoginDataEvent
import com.jiachian.nbatoday.common.ui.login.event.LoginUIEvent
import com.jiachian.nbatoday.common.ui.login.state.LoginState
import com.jiachian.nbatoday.common.ui.login.state.MutableLoginState
import com.jiachian.nbatoday.home.user.domain.UserUseCase
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
