package com.jiachian.nbatoday.data.local.datasource.game

import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import kotlinx.coroutines.flow.Flow

abstract class GameLocalSource {
    abstract suspend fun getGamesAt(date: Long): List<NbaGame>
    abstract fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>
    abstract fun getGamesAndBets(): Flow<List<NbaGameAndBet>>
    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>>
    abstract fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>
    abstract fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>
    abstract fun getGamesBefore(from: Long): Flow<List<NbaGame>>
    abstract fun getGamesAfter(from: Long): Flow<List<NbaGame>>

    abstract suspend fun insertGames(games: List<NbaGame>)
    abstract suspend fun updateGames(games: List<GameUpdateData>)

    abstract suspend fun existsGame(): Boolean
}
