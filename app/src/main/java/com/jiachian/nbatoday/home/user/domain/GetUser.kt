package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.home.user.data.UserRepository
import com.jiachian.nbatoday.home.user.data.model.local.User
import kotlinx.coroutines.flow.Flow

class GetUser(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<User?> = repository.user
}
