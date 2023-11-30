package com.jiachian.nbatoday.repository.bet

import com.jiachian.nbatoday.datasource.local.bet.BetLocalSource
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NbaBetRepository(
    private val betLocalSource: BetLocalSource,
    private val userRepository: UserRepository,
) : BetRepository() {
    private val user = userRepository.user

    override suspend fun bet(gameId: String, homePoints: Long, awayPoints: Long) {
        val user = user.firstOrNull()
        val account = user?.account ?: return
        val remainPoints = (user.points ?: 0) - homePoints - awayPoints
        if (remainPoints < 0) return
        isProgressingImp.value = true
        val bet = Bet(
            account = account,
            gameId = gameId,
            homePoints = homePoints,
            awayPoints = awayPoints
        )
        betLocalSource.insertBet(bet)
        userRepository.updatePoints(remainPoints)
        isProgressingImp.value = false
    }

    override suspend fun deleteBets(bet: Bet) {
        betLocalSource.deleteBet(bet)
    }

    override suspend fun settleBet(betAndGame: BetAndGame): Pair<Long, Long> {
        val winPoint = (
            if (betAndGame.game.homeTeam.score > betAndGame.game.awayTeam.score) {
                betAndGame.bet.homePoints
            } else {
                betAndGame.bet.awayPoints
            }
            ) * 2
        val losePoint = if (betAndGame.game.homeTeam.score > betAndGame.game.awayTeam.score) {
            betAndGame.bet.awayPoints
        } else {
            betAndGame.bet.homePoints
        }
        userRepository.addPoints(winPoint)
        deleteBets(betAndGame.bet)
        return winPoint to losePoint
    }

    override suspend fun addPoints(points: Long) {
        userRepository.addPoints(points)
    }

    override fun getBetsAndGames(account: String): Flow<List<BetAndGame>> {
        return betLocalSource.getBetsAndGamesByUser(account)
    }
}
