package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.bet.data.BetDao
import com.jiachian.nbatoday.bet.data.model.local.Bet
import com.jiachian.nbatoday.bet.data.model.local.BetAndGame
import com.jiachian.nbatoday.datasource.local.bet.BetLocalSource
import kotlinx.coroutines.flow.Flow

class TestBetLocalSource(
    private val betDao: BetDao
) : BetLocalSource() {
    override fun getBetsAndGames(account: String): Flow<List<BetAndGame>> {
        return betDao.getBetsAndGames(account)
    }

    override suspend fun insertBet(bet: Bet) {
        betDao.addBet(bet)
    }

    override suspend fun deleteBet(bet: Bet) {
        betDao.deleteBet(bet)
    }
}
