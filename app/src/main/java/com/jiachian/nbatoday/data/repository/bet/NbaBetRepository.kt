package com.jiachian.nbatoday.data.repository.bet

import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.datasource.bet.BetLocalSource
import com.jiachian.nbatoday.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NbaBetRepository(
    private val betLocalSource: BetLocalSource,
    private val userRepository: UserRepository,
) : BetRepository() {
    private val user = userRepository.user

    override suspend fun bet(gameId: String, homePoints: Long, awayPoints: Long) {
        val user = user.firstOrNull() ?: return
        val account = user.account ?: return
        val remainPoints = (user.points ?: 0) - homePoints - awayPoints
        if (remainPoints < 0) return
        isProgressingImp.value = true
        betLocalSource.insertBet(account, gameId, homePoints, awayPoints)
        userRepository.updatePoints(remainPoints)
        isProgressingImp.value = false
    }

    override suspend fun deleteBets(bets: Bets) {
        betLocalSource.deleteBets(bets)
    }

    override suspend fun settleBet(betAndGame: BetAndNbaGame): Pair<Long, Long> {
        val winPoint = (
            if (betAndGame.game.homeTeam.score > betAndGame.game.awayTeam.score) {
                betAndGame.bets.homePoints
            } else {
                betAndGame.bets.awayPoints
            }
            ) * 2
        val losePoint = if (betAndGame.game.homeTeam.score > betAndGame.game.awayTeam.score) {
            betAndGame.bets.awayPoints
        } else {
            betAndGame.bets.homePoints
        }
        userRepository.addPoints(winPoint)
        deleteBets(betAndGame.bets)
        return winPoint to losePoint
    }

    override suspend fun addPoints(points: Long) {
        userRepository.addPoints(points)
    }

    override fun getBetsAndGames(account: String): Flow<List<BetAndNbaGame>> {
        return betLocalSource.getBetsAndGamesByUser(account)
    }
}
