package com.jiachian.nbatoday.compose.screen.account

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jiachian.nbatoday.usecase.user.UserUseCase

class LoginDialogViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val accountImp = mutableStateOf("")
    val account: State<String> = accountImp

    private val passwordImp = mutableStateOf("")
    val password: State<String> = passwordImp

    val valid = derivedStateOf { account.value.isNotBlank() && password.value.isNotBlank() }

    fun updateAccount(account: String) {
        accountImp.value = account
    }

    fun updatePassword(password: String) {
        passwordImp.value = password
    }

    suspend fun login() {
        userUseCase.userLogin(account.value, password.value)
    }

    suspend fun register() {
        userUseCase.userRegister(account.value, password.value)
    }
}
