package com.jiachian.nbatoday.datasource.local.boxscore

import com.jiachian.nbatoday.database.dao.BoxScoreDao
import com.jiachian.nbatoday.models.local.score.BoxScore
import kotlinx.coroutines.flow.Flow

class NbaBoxScoreLocalSource(
    private val boxScoreDao: BoxScoreDao,
) : BoxScoreLocalSource() {
    override fun getGameBoxScore(gameId: String): Flow<BoxScore?> {
        return boxScoreDao.getBoxScore(gameId)
    }

    override suspend fun insertGameBoxScore(boxScore: BoxScore) {
        boxScoreDao.insertBoxScore(boxScore)
    }
}
