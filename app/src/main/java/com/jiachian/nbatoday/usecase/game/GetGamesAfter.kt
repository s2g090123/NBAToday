package com.jiachian.nbatoday.usecase.game

import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.repository.game.GameRepository
import kotlinx.coroutines.flow.Flow

class GetGamesAfter(
    private val repository: GameRepository
) {
    operator fun invoke(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return repository.getGamesAndBetsAfter(teamId, from)
    }
}
