package com.jiachian.nbatoday.game.domain

import com.jiachian.nbatoday.game.data.GameRepository
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import kotlinx.coroutines.flow.Flow

class GetGamesDuring(
    private val repository: GameRepository
) {
    operator fun invoke(from: Long, to: Long): Flow<List<GameAndBets>> {
        return repository.getGamesAndBetsDuring(from, to)
    }
}
