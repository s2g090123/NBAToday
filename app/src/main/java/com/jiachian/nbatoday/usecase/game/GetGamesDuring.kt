package com.jiachian.nbatoday.usecase.game

import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.repository.game.GameRepository
import kotlinx.coroutines.flow.Flow

class GetGamesDuring(
    private val repository: GameRepository
) {
    operator fun invoke(from: Long, to: Long): Flow<List<GameAndBets>> {
        return repository.getGamesAndBetsDuring(from, to)
    }
}
