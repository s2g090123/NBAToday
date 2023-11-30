package com.jiachian.nbatoday.datasource.local.boxscore

import com.jiachian.nbatoday.models.local.score.BoxScore
import kotlinx.coroutines.flow.Flow

abstract class BoxScoreLocalSource {
    abstract fun getBoxScore(gameId: String): Flow<BoxScore?>

    abstract suspend fun insertBoxScore(boxScore: BoxScore)
}
