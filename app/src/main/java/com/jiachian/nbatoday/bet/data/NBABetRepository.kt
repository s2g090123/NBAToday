package com.jiachian.nbatoday.bet.data

import com.jiachian.nbatoday.bet.data.model.local.Bet
import com.jiachian.nbatoday.bet.data.model.local.BetAndGame
import kotlinx.coroutines.flow.Flow

class NBABetRepository(
    private val dao: BetDao,
) : BetRepository {
    override suspend fun addBet(bet: Bet) {
        dao.addBet(bet)
    }

    override suspend fun deleteBet(bet: Bet) {
        dao.deleteBet(bet)
    }

    override fun getBetGames(account: String): Flow<List<BetAndGame>> {
        return dao.getBetsAndGames(account)
    }
}
