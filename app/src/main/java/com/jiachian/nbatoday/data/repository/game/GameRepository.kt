package com.jiachian.nbatoday.data.repository.game

import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class GameRepository : BaseRepository() {
    abstract suspend fun refreshGameBoxScore(gameId: String)

    abstract suspend fun getGamesAt(date: Long): List<NbaGame>
    abstract fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>
    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>>
    abstract fun getGamesBefore(from: Long): Flow<List<NbaGame>>
    abstract fun getGamesAfter(from: Long): Flow<List<NbaGame>>
    abstract fun getGameBoxScore(gameId: String): Flow<GameBoxScore?>
    abstract fun getGamesAndBets(): Flow<List<NbaGameAndBet>>
    abstract fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>
    abstract fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>
}
