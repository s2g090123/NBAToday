package com.jiachian.nbatoday.usecase.bet

import com.jiachian.nbatoday.repository.bet.BetRepository

class AddBet(
    private val repository: BetRepository
) {
    suspend operator fun invoke(
        gameId: String,
        account: String,
        homePoints: Long,
        awayPoints: Long,
    ) {
        repository.addBet(gameId, homePoints, awayPoints)
    }
}
