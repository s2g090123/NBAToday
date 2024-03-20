package com.jiachian.nbatoday.compose.screen.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.compose.screen.account.event.LoginEvent
import com.jiachian.nbatoday.compose.screen.account.state.LoginState
import com.jiachian.nbatoday.compose.screen.account.state.MutableLoginState
import com.jiachian.nbatoday.usecase.user.UserUseCase
import kotlinx.coroutines.launch

class LoginDialogViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val stateImp = MutableLoginState()
    val state: LoginState = stateImp

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.Login -> login()
            LoginEvent.Register -> register()
            is LoginEvent.TextAccount -> stateImp.account = event.account
            is LoginEvent.TextPassword -> stateImp.password = event.password
            LoginEvent.ErrorSeen -> stateImp.error = null
        }
    }

    private fun login() {
        viewModelScope.launch {
            when (val resource = userUseCase.userLogin(state.account, state.password)) {
                is Resource.Error -> {
                    stateImp.error = resource.message
                }
                is Resource.Loading -> Unit
                is Resource.Success -> {
                    stateImp.isLogin = true
                }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            when (val resource = userUseCase.userRegister(state.account, state.password)) {
                is Resource.Error -> {
                    stateImp.error = resource.message
                }
                is Resource.Loading -> Unit
                is Resource.Success -> {
                    stateImp.isLogin = true
                }
            }
        }
    }
}
