package com.jiachian.nbatoday.usecase.bet

import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.repository.bet.BetRepository
import kotlinx.coroutines.flow.Flow

class GetBetGames(
    private val repository: BetRepository,
) {
    operator fun invoke(account: String): Flow<List<BetAndGame>> = repository.getBetGames(account)
}
