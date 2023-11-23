package com.jiachian.nbatoday.data.local.datasource.bet

import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.local.NbaDao
import com.jiachian.nbatoday.data.local.bet.Bets
import kotlinx.coroutines.flow.Flow

class NbaBetLocalSource(
    private val dao: NbaDao,
) : BetLocalSource() {
    override fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>> {
        return dao.getBetsAndGamesByUser(account)
    }

    override suspend fun insertBet(account: String, gameId: String, homePoints: Long, awayPoints: Long) {
        val bets = Bets(
            account = account,
            gameId = gameId,
            homePoints = homePoints,
            awayPoints = awayPoints
        )
        dao.insertBet(bets)
    }

    override suspend fun deleteBets(bets: Bets) {
        dao.deleteBet(bets)
    }
}
