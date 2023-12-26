package com.jiachian.nbatoday.repository.bet

import com.jiachian.nbatoday.datasource.local.bet.BetLocalSource
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NBABetRepository(
    private val betLocalSource: BetLocalSource,
    private val userRepository: UserRepository,
) : BetRepository() {
    override suspend fun insertBet(gameId: String, homePoints: Long, awayPoints: Long) {
        loading {
            val usedPoints = homePoints + awayPoints
            userRepository
                .user
                .firstOrNull()
                ?.takeIf { user -> user.available }
                ?.takeIf { user -> user.points - usedPoints >= 0 }
                ?.also { user ->
                    betLocalSource.insertBet(
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
        loading {
            betLocalSource.deleteBet(bet)
        }
    }

    override suspend fun settleBet(betAndGame: BetAndGame): Pair<Long, Long> {
        return loading {
            val wonPoints = betAndGame.getWonPoints() * 2
            val lostPoints = betAndGame.getLostPoints()
            userRepository.addPoints(wonPoints)
            deleteBet(betAndGame.bet)
            wonPoints to lostPoints
        }
    }

    override suspend fun addPoints(points: Long) {
        loading {
            userRepository.addPoints(points)
        }
    }

    override fun getBetsAndGames(account: String): Flow<List<BetAndGame>> {
        return betLocalSource.getBetsAndGames(account)
    }
}
