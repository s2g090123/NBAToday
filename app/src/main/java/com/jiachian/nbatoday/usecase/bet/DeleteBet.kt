package com.jiachian.nbatoday.usecase.bet

import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.repository.bet.BetRepository

class DeleteBet(
    private val repository: BetRepository
) {
    suspend operator fun invoke(bet: Bet) {
        repository.deleteBet(bet)
    }
}
