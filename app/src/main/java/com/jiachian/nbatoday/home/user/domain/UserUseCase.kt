package com.jiachian.nbatoday.home.user.domain

data class UserUseCase(
    val getUser: GetUser,
    val addPoints: AddPoints,
    val userLogin: UserLogin,
    val userRegister: UserRegister,
    val userLogout: UserLogout,
    val updateTheme: UpdateTheme,
    val getTheme: GetTheme,
)
