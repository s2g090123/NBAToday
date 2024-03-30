package com.jiachian.nbatoday.usecase.game

import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.repository.game.GameRepository

class GetGame(
    private val repository: GameRepository,
) {
    suspend operator fun invoke(gameId: String): GameAndBets {
        return repository.getGameAndBets(gameId)
    }
}
