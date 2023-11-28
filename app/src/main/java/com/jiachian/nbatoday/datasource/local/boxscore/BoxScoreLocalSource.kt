package com.jiachian.nbatoday.datasource.local.boxscore

import com.jiachian.nbatoday.models.local.score.BoxScore
import kotlinx.coroutines.flow.Flow

abstract class BoxScoreLocalSource {
    abstract fun getGameBoxScore(gameId: String): Flow<BoxScore?>

    abstract suspend fun insertGameBoxScore(boxScore: BoxScore)
}
