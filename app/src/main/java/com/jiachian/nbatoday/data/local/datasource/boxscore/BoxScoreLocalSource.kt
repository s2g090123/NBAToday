package com.jiachian.nbatoday.data.local.datasource.boxscore

import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
import kotlinx.coroutines.flow.Flow

abstract class BoxScoreLocalSource {
    abstract fun getGameBoxScore(gameId: String): Flow<GameBoxScore?>

    abstract suspend fun insertGameBoxScore(boxScore: GameBoxScore)
    abstract suspend fun updateGamesScore(games: List<GameScoreUpdateData>)
}
