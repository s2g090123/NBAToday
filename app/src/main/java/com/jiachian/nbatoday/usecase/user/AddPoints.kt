package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.repository.user.UserRepository

class AddPoints(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(points: Long) {
        userRepository.addPoints(points)
    }
}
