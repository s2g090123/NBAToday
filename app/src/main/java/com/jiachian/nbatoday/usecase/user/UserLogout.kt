package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.repository.user.UserRepository

class UserLogout(
    private val repository: UserRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
