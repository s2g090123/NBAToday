package com.jiachian.nbatoday.datasource.local.bet

import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndNbaGame
import kotlinx.coroutines.flow.Flow

abstract class BetLocalSource {
    abstract fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>>

    abstract suspend fun insertBet(
        account: String,
        gameId: String,
        homePoints: Long,
        awayPoints: Long
    )

    abstract suspend fun deleteBets(bet: Bet)
}
