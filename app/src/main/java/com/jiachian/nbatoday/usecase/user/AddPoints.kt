package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.repository.user.UserRepository

enum class AddPointsError : Error {
    ADD_FAILED
}

class AddPoints(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(points: Long): Resource<Unit, AddPointsError> {
        return when (userRepository.addPoints(points)) {
            is Response.Error -> Resource.Error(AddPointsError.ADD_FAILED)
            is Response.Success -> Resource.Success(Unit)
        }
    }
}
