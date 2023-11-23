package com.jiachian.nbatoday.data.local.datasource.bet

import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.dao.BetDao
import kotlinx.coroutines.flow.Flow

class NbaBetLocalSource(
    private val betDao: BetDao,
) : BetLocalSource() {
    override fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>> {
        return betDao.getBetsAndGamesByUser(account)
    }

    override suspend fun insertBet(account: String, gameId: String, homePoints: Long, awayPoints: Long) {
        val bets = Bets(
            account = account,
            gameId = gameId,
            homePoints = homePoints,
            awayPoints = awayPoints
        )
        betDao.insertBet(bets)
    }

    override suspend fun deleteBets(bets: Bets) {
        betDao.deleteBet(bets)
    }
}
