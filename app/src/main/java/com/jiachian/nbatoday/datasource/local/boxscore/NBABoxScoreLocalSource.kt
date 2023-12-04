package com.jiachian.nbatoday.datasource.local.boxscore

import com.jiachian.nbatoday.database.dao.BoxScoreDao
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import kotlinx.coroutines.flow.Flow

class NBABoxScoreLocalSource(
    private val boxScoreDao: BoxScoreDao,
) : BoxScoreLocalSource() {
    override fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?> {
        return boxScoreDao.getBoxScoreAndGame(gameId)
    }

    override suspend fun insertBoxScore(boxScore: BoxScore) {
        boxScoreDao.insertBoxScore(boxScore)
    }
}
