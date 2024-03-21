package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.repository.user.UserRepository

enum class UserRegisterError : Error {
    ACCOUNT_EMPTY,
    PASSWORD_EMPTY,
    LOGIN_FAILED,
}

class UserRegister(
    private val repository: UserRepository
) {
    suspend operator fun invoke(account: String, password: String): Resource<User, UserRegisterError> {
        if (account.isBlank()) {
            return Resource.Error(UserRegisterError.ACCOUNT_EMPTY)
        }
        if (password.isBlank()) {
            return Resource.Error(UserRegisterError.PASSWORD_EMPTY)
        }
        return when (val response = repository.register(account, password)) {
            is Response.Error -> Resource.Error(UserRegisterError.LOGIN_FAILED)
            is Response.Success -> Resource.Success(response.data)
        }
    }
}
