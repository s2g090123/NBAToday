package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.common.Resource2
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.repository.user.UserRepository

class UserLogin(
    private val repository: UserRepository
) {
    suspend operator fun invoke(account: String, password: String): Resource2<User> {
        if (account.isBlank()) {
            return Resource2.Error("Account is empty.")
        }
        if (password.isBlank()) {
            return Resource2.Error("Password is empty.")
        }
        return when (val response = repository.login(account, password)) {
            is Response.Error -> Resource2.Error("Login failed.")
            is Response.Success -> Resource2.Success(response.data)
        }
    }
}
