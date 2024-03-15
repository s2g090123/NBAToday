package com.jiachian.nbatoday.repository.bet

import com.jiachian.nbatoday.database.dao.BetDao
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import kotlinx.coroutines.flow.Flow

class NBABetRepository(
    private val dao: BetDao,
) : BetRepository() {
    override suspend fun addBet(gameId: String, account: String, homePoints: Long, awayPoints: Long) {
        loading {
            dao.addBet(
                Bet(
                    account = account,
                    gameId = gameId,
                    homePoints = homePoints,
                    awayPoints = awayPoints
                )
            )
        }
    }

    override suspend fun deleteBet(bet: Bet) {
        dao.deleteBet(bet)
    }

    override fun getBetGames(account: String): Flow<List<BetAndGame>> = dao.getBetsAndGames(account)
}
