package com.jiachian.nbatoday.data.local.datasource.boxscore

import com.jiachian.nbatoday.data.local.dao.BoxScoreDao
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
import kotlinx.coroutines.flow.Flow

class NbaBoxScoreLocalSource(
    private val boxScoreDao: BoxScoreDao,
) : BoxScoreLocalSource() {
    override fun getGameBoxScore(gameId: String): Flow<GameBoxScore?> {
        return boxScoreDao.getGameBoxScore(gameId)
    }

    override suspend fun insertGameBoxScore(boxScore: GameBoxScore) {
        boxScoreDao.insertGameBoxScore(boxScore)
    }

    override suspend fun updateGamesScore(games: List<GameScoreUpdateData>) {
        boxScoreDao.updateGamesScore(games)
    }
}
