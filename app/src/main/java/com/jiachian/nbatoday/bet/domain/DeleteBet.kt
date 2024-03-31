package com.jiachian.nbatoday.bet.domain

import com.jiachian.nbatoday.bet.data.BetRepository
import com.jiachian.nbatoday.bet.data.model.local.Bet

class DeleteBet(
    private val repository: BetRepository
) {
    suspend operator fun invoke(bet: Bet) {
        repository.deleteBet(bet)
    }
}
