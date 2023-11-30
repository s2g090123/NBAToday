package com.jiachian.nbatoday.repository.bet

import com.jiachian.nbatoday.datasource.local.bet.BetLocalSource
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.repository.user.UserRepository
import com.jiachian.nbatoday.utils.showErrorToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NBABetRepository(
    private val betLocalSource: BetLocalSource,
    private val userRepository: UserRepository,
) : BetRepository() {
    private val user = userRepository.user

    override suspend fun bet(gameId: String, homePoints: Long, awayPoints: Long) {
        loading {
            val user = user.firstOrNull() ?: run {
                showErrorToast()
                return@loading
            }
            val remainPoints = user.points - homePoints - awayPoints
            if (remainPoints < 0) {
                showErrorToast()
                return@loading
            }
            val bet = Bet(
                account = user.account,
                gameId = gameId,
                homePoints = homePoints,
                awayPoints = awayPoints
            )
            betLocalSource.insertBet(bet)
            userRepository.updatePoints(remainPoints)
        }
    }

    override suspend fun deleteBet(bet: Bet) {
        loading {
            betLocalSource.deleteBet(bet)
        }
    }

    override suspend fun settleBet(betAndGame: BetAndGame): Pair<Long, Long> {
        return loading {
            val winPoint = betAndGame.getWinnerPoints() * 2
            val losePoint = betAndGame.getLoserPoints()
            userRepository.addPoints(winPoint)
            deleteBet(betAndGame.bet)
            winPoint to losePoint
        }
    }

    override suspend fun addPoints(points: Long) {
        loading {
            userRepository.addPoints(points)
        }
    }

    override fun getBetsAndGames(account: String): Flow<List<BetAndGame>> {
        return betLocalSource.getBetsAndGamesByUser(account)
    }
}
