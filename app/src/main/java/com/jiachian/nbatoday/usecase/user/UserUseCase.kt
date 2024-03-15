package com.jiachian.nbatoday.usecase.user

data class UserUseCase(
    val getUser: GetUser,
    val addPoints: AddPoints,
    val userLogin: UserLogin,
    val userRegister: UserRegister,
)
