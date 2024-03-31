package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.boxscore.data.BoxScoreDao
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScore
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScoreAndGame
import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import kotlinx.coroutines.flow.Flow

class TestBoxScoreLocalSource(
    private val boxScoreDao: BoxScoreDao
) : BoxScoreLocalSource() {
    override fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?> {
        return boxScoreDao.getBoxScoreAndGame(gameId)
    }

    override suspend fun insertBoxScore(boxScore: BoxScore) {
        boxScoreDao.insertBoxScore(boxScore)
    }
}
