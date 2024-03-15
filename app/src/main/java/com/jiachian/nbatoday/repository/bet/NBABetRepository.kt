package com.jiachian.nbatoday.repository.bet

import com.jiachian.nbatoday.database.dao.BetDao
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NBABetRepository(
    private val dao: BetDao,
    private val userRepository: UserRepository,
) : BetRepository() {
    override suspend fun addBet(gameId: String, homePoints: Long, awayPoints: Long) {
        loading {
            val usedPoints = homePoints + awayPoints
            userRepository
                .user
                .firstOrNull()
                ?.takeIf { user -> user.available }
                ?.takeIf { user -> user.points - usedPoints >= 0 }
                ?.also { user ->
                    dao.addBet(
                        Bet(
                            account = user.account,
                            gameId = gameId,
                            homePoints = homePoints,
                            awayPoints = awayPoints
                        )
                    )
                    userRepository.updatePoints(user.points - usedPoints)
                }
                ?: onError()
        }
    }

    override suspend fun deleteBet(bet: Bet) {
        dao.deleteBet(bet)
    }

    override fun getBetGames(account: String): Flow<List<BetAndGame>> = dao.getBetsAndGames(account)
}
