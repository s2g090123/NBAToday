package com.jiachian.nbatoday.usecase.game

import com.jiachian.nbatoday.repository.game.GameRepository

class GetGame(
    private val repository: GameRepository,
) {
    suspend operator fun invoke(gameId: String) = repository.getGameAndBet(gameId)
}
