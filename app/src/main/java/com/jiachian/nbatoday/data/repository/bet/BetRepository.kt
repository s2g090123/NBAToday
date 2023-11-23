package com.jiachian.nbatoday.data.repository.bet

import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class BetRepository : BaseRepository() {
    abstract suspend fun bet(gameId: String, homePoints: Long, awayPoints: Long)
    abstract suspend fun deleteBets(bets: Bets)
    abstract suspend fun settleBet(betAndGame: BetAndNbaGame): Pair<Long, Long>
    abstract suspend fun addPoints(points: Long)

    abstract fun getBetsAndGames(account: String): Flow<List<BetAndNbaGame>>
}
