package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.home.user.data.UserRepository
import com.jiachian.nbatoday.home.user.data.model.local.User
import com.jiachian.nbatoday.home.user.domain.error.UserLoginError

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
