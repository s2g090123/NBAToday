package com.jiachian.nbatoday.game.domain

import com.jiachian.nbatoday.game.data.GameRepository
import com.jiachian.nbatoday.game.data.model.local.GameAndBets

class GetGame(
    private val repository: GameRepository,
) {
    suspend operator fun invoke(gameId: String): GameAndBets {
        return repository.getGameAndBets(gameId)
    }
}
