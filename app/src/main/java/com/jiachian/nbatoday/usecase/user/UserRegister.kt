package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.repository.user.UserRepository

class UserRegister(
    private val repository: UserRepository
) {
    suspend operator fun invoke(account: String, password: String): Resource<User> {
        if (account.isBlank()) {
            return Resource.Error("Account is empty.")
        }
        if (password.isBlank()) {
            return Resource.Error("Password is empty.")
        }
        return when (val response = repository.register(account, password)) {
            is Response.Error -> Resource.Error("Login failed.")
            is Response.Success -> Resource.Success(response.data)
        }
    }
}
