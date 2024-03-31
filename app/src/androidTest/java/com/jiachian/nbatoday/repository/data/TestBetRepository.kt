package com.jiachian.nbatoday.repository.data

import com.jiachian.nbatoday.ComingSoonBetId
import com.jiachian.nbatoday.FinalBetId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.PlayingBetId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.bet.data.BetRepository
import com.jiachian.nbatoday.bet.data.model.local.Bet
import com.jiachian.nbatoday.bet.data.model.local.BetAndGame
import com.jiachian.nbatoday.datasource.local.bet.BetLocalSource
import com.jiachian.nbatoday.home.user.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class TestBetRepository(
    private val betLocalSource: BetLocalSource,
    private val userRepository: UserRepository,
) : BetRepository() {
    override suspend fun addBet(gameId: String, homePoints: Long, awayPoints: Long) {
        val usedPoints = homePoints + awayPoints
        userRepository
            .user
            .firstOrNull()
            ?.takeIf { user -> user.available }
            ?.takeIf { user -> user.points - usedPoints >= 0 }
            ?.let { user ->
                val betId = when (gameId) {
                    FinalGameId -> FinalBetId
                    PlayingGameId -> PlayingBetId
                    else -> ComingSoonBetId
                }
                betLocalSource.insertBet(
                    Bet(
                        betId = betId,
                        account = user.account,
                        gameId = gameId,
                        homePoints = homePoints,
                        awayPoints = awayPoints
                    )
                )
                userRepository.updatePoints(user.points - usedPoints)
            }
    }

    override suspend fun deleteBet(bet: Bet) {
        betLocalSource.deleteBet(bet)
    }

    override suspend fun settleBet(betAndGame: BetAndGame): Pair<Long, Long> {
        val wonPoints = betAndGame.getWonPoints() * 2
        val lostPoints = betAndGame.getLostPoints()
        userRepository.addPoints(wonPoints)
        deleteBet(betAndGame.bet)
        return wonPoints to lostPoints
    }

    override suspend fun addPoints(points: Long) {
        userRepository.addPoints(points)
    }

    override fun getBetGames(account: String): Flow<List<BetAndGame>> {
        return betLocalSource.getBetsAndGames(account)
    }
}
