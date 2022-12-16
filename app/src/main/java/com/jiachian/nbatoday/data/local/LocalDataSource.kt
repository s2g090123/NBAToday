package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import kotlinx.coroutines.flow.Flow
import java.util.*

abstract class LocalDataSource {

    abstract val dates: Flow<List<Date>>

    abstract val games: Flow<List<NbaGame>>

    abstract fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>

    abstract suspend fun existsData(): Boolean

    abstract suspend fun insertGames(games: List<NbaGame>)

    abstract suspend fun updateGame(status: List<GameUpdateData>)

    abstract fun getGameBoxScore(gameId: String): Flow<GameBoxScore?>
    abstract suspend fun insertGameBoxScore(boxScore: GameBoxScore)
}