package com.jiachian.nbatoday.datasource.local.bet

import com.jiachian.nbatoday.database.dao.BetDao
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndNbaGame
import kotlinx.coroutines.flow.Flow

class NbaBetLocalSource(
    private val betDao: BetDao,
) : BetLocalSource() {
    override fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>> {
        return betDao.getBetsAndGamesByUser(account)
    }

    override suspend fun insertBet(
        account: String,
        gameId: String,
        homePoints: Long,
        awayPoints: Long
    ) {
        val bet = Bet(
            account = account,
            gameId = gameId,
            homePoints = homePoints,
            awayPoints = awayPoints
        )
        betDao.insertBet(bet)
    }

    override suspend fun deleteBets(bet: Bet) {
        betDao.deleteBet(bet)
    }
}
