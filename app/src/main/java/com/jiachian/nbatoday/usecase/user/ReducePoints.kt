package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.repository.user.UserRepository

class ReducePoints(
    private val repository: UserRepository
) {
    suspend operator fun invoke(points: Long) {
        repository.addPoints(-points)
    }
}
