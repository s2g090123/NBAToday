package com.jiachian.nbatoday.usecase.boxscore

import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import com.jiachian.nbatoday.repository.game.GameRepository
import kotlinx.coroutines.flow.Flow

class GetBoxScore(
    private val repository: GameRepository
) {
    operator fun invoke(gameId: String): Flow<BoxScoreAndGame?> {
        return repository.getBoxScoreAndGame(gameId)
    }
}
