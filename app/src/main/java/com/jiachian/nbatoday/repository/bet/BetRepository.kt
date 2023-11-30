package com.jiachian.nbatoday.repository.bet

import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class BetRepository : BaseRepository() {
    abstract suspend fun bet(gameId: String, homePoints: Long, awayPoints: Long)
    abstract suspend fun deleteBets(bet: Bet)
    abstract suspend fun settleBet(betAndGame: BetAndGame): Pair<Long, Long>
    abstract suspend fun addPoints(points: Long)

    abstract fun getBetsAndGames(account: String): Flow<List<BetAndGame>>
}
