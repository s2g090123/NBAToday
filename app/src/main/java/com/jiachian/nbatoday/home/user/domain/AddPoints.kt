package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.home.user.data.UserRepository
import com.jiachian.nbatoday.home.user.domain.error.AddPointsError

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
