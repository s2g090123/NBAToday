package com.jiachian.nbatoday.datasource.local.game

import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBet
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import kotlinx.coroutines.flow.Flow

abstract class GameLocalSource {
    abstract fun getGamesAndBets(): Flow<List<GameAndBet>>
    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBet>>
    abstract fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<GameAndBet>>
    abstract fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<GameAndBet>>

    abstract suspend fun insertGames(games: List<Game>)
    abstract suspend fun updateGames(games: List<GameUpdateData>)
    abstract suspend fun updateGamesScore(games: List<GameScoreUpdateData>)

    abstract suspend fun existsGame(): Boolean
}