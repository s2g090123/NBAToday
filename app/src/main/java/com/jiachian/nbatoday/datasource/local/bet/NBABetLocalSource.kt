package com.jiachian.nbatoday.datasource.local.bet

import com.jiachian.nbatoday.database.dao.BetDao
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import kotlinx.coroutines.flow.Flow

class NBABetLocalSource(
    private val betDao: BetDao,
) : BetLocalSource() {
    override fun getBetsAndGames(account: String): Flow<List<BetAndGame>> {
        return betDao.getBetsAndGames(account)
    }

    override suspend fun insertBet(bet: Bet) {
        betDao.insertBet(bet)
    }

    override suspend fun deleteBet(bet: Bet) {
        betDao.deleteBet(bet)
    }
}
