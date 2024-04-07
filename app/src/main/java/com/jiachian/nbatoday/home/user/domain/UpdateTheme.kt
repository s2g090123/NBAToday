package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.home.user.data.UserRepository

class UpdateTheme(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(teamId: Int) {
        repository.updateTheme(teamId)
    }
}
