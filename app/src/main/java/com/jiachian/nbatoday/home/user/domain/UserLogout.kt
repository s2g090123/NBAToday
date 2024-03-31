package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.home.user.data.UserRepository

class UserLogout(
    private val repository: UserRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
