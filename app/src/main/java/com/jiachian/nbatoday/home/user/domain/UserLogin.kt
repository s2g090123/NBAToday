package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.home.user.data.UserRepository
import com.jiachian.nbatoday.home.user.domain.error.UserLoginError

class UserLogin(
    private val repository: UserRepository
) {
    suspend operator fun invoke(account: String, password: String): Resource<Unit, UserLoginError> {
        if (account.isBlank() || password.isBlank()) {
            return Resource.Error(UserLoginError.INVALID_ACCOUNT)
        }
        return when (repository.login(account, password)) {
            is Response.Error -> Resource.Error(UserLoginError.LOGIN_FAILED)
            is Response.Success -> Resource.Success(Unit)
        }
    }
}
