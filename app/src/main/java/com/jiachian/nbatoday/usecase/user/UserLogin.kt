package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.repository.user.UserRepository

enum class UserLoginError : Error {
    ACCOUNT_EMPTY,
    PASSWORD_EMPTY,
    LOGIN_FAILED,
}

class UserLogin(
    private val repository: UserRepository
) {
    suspend operator fun invoke(account: String, password: String): Resource<User, UserLoginError> {
        if (account.isBlank()) {
            return Resource.Error(UserLoginError.ACCOUNT_EMPTY)
        }
        if (password.isBlank()) {
            return Resource.Error(UserLoginError.PASSWORD_EMPTY)
        }
        return when (val response = repository.login(account, password)) {
            is Response.Error -> Resource.Error(UserLoginError.LOGIN_FAILED)
            is Response.Success -> Resource.Success(response.data)
        }
    }
}
