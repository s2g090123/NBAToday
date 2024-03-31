package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.home.user.data.UserRepository
import com.jiachian.nbatoday.home.user.data.model.local.User
import com.jiachian.nbatoday.home.user.domain.error.UserRegisterError

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
