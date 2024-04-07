package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.home.user.data.UserRepository
import kotlinx.coroutines.flow.Flow

class GetTheme(
    private val repository: UserRepository,
) {
    operator fun invoke(): Flow<NBAColors> {
        return repository.theme
    }
}
