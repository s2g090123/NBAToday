package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUser(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<User?> = repository.user
}
