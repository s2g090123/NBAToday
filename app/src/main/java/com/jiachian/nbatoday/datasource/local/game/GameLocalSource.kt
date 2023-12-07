package com.jiachian.nbatoday.datasource.local.game

import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import java.util.Date
import kotlinx.coroutines.flow.Flow

abstract class GameLocalSource {
    abstract fun getGamesAndBets(): Flow<List<GameAndBets>>
    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>>
    abstract fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<GameAndBets>>
    abstract fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<GameAndBets>>
    abstract fun getLastGameDateTime(): Flow<Date>
    abstract fun getFirstGameDateTime(): Flow<Date>

    abstract suspend fun updateGames(games: List<GameUpdateData>)
    abstract suspend fun updateGamesScore(games: List<GameScoreUpdateData>)

    abstract suspend fun existsGame(): Boolean
}
