package com.jiachian.nbatoday.game.domain

import com.jiachian.nbatoday.game.data.GameRepository
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import kotlinx.coroutines.flow.Flow

class GetGamesBefore(
    private val repository: GameRepository
) {
    operator fun invoke(teamId: Int, from: Long): Flow<List<GameAndBets>> {
        return repository.getGamesAndBetsBefore(teamId, from)
    }
}
