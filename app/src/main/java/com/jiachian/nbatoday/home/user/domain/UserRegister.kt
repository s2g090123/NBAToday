package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.home.user.data.UserRepository
import com.jiachian.nbatoday.home.user.domain.error.UserRegisterError

class UserRegister(
    private val repository: UserRepository
) {
    suspend operator fun invoke(account: String, password: String): Resource<Unit, UserRegisterError> {
        if (account.isBlank() || password.isBlank()) {
            return Resource.Error(UserRegisterError.INVALID_ACCOUNT)
        }
        return when (repository.register(account, password)) {
            is Response.Error -> Resource.Error(UserRegisterError.REGISTER_FAILED)
            is Response.Success -> Resource.Success(Unit)
        }
    }
}
