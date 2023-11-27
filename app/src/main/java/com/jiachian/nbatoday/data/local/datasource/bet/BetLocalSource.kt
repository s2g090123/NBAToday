package com.jiachian.nbatoday.data.local.datasource.bet

import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.local.bet.Bets
import kotlinx.coroutines.flow.Flow

abstract class BetLocalSource {
    abstract fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>>

    abstract suspend fun insertBet(
        account: String,
        gameId: String,
        homePoints: Long,
        awayPoints: Long
    )

    abstract suspend fun deleteBets(bets: Bets)
}
