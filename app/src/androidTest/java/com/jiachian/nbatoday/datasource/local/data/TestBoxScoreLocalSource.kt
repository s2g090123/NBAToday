package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.database.dao.BoxScoreDao
import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
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
