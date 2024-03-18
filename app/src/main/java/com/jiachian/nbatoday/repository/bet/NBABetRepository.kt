package com.jiachian.nbatoday.repository.bet

import com.jiachian.nbatoday.database.dao.BetDao
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import kotlinx.coroutines.flow.Flow

class NBABetRepository(
    private val dao: BetDao,
) : BetRepository() {
    override suspend fun addBet(bet: Bet) {
        dao.addBet(bet)
    }

    override suspend fun deleteBet(bet: Bet) {
        dao.deleteBet(bet)
    }

    override fun getBetGames(account: String): Flow<List<BetAndGame>> = dao.getBetsAndGames(account)
}
