package com.jiachian.nbatoday.datasource.local.boxscore

import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import kotlinx.coroutines.flow.Flow

abstract class BoxScoreLocalSource {
    abstract fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?>

    abstract suspend fun insertBoxScore(boxScore: BoxScore)
}
