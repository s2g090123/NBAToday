package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.repository.user.UserRepository

class UserRegister(
    private val repository: UserRepository
) {
    suspend operator fun invoke(account: String, password: String) {
        if (account.isBlank()) {
            throw Exception("Account is empty.")
        }
        if (password.isBlank()) {
            throw Exception("Password is empty.")
        }
        repository.register(account, password)
    }
}
