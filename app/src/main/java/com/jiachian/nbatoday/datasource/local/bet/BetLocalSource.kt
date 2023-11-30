package com.jiachian.nbatoday.datasource.local.bet

import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import kotlinx.coroutines.flow.Flow

abstract class BetLocalSource {
    abstract fun getBetsAndGamesByUser(account: String): Flow<List<BetAndGame>>

    abstract suspend fun insertBet(bet: Bet)

    abstract suspend fun deleteBet(bet: Bet)
}
