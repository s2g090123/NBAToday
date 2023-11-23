package com.jiachian.nbatoday.data.local.datasource.boxscore

import com.jiachian.nbatoday.data.local.NbaDao
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
import kotlinx.coroutines.flow.Flow

class NbaBoxScoreLocalSource(
    private val dao: NbaDao,
) : BoxScoreLocalSource() {
    override fun getGameBoxScore(gameId: String): Flow<GameBoxScore?> {
        return dao.getGameBoxScore(gameId)
    }

    override suspend fun insertGameBoxScore(boxScore: GameBoxScore) {
        dao.insertGameBoxScore(boxScore)
    }

    override suspend fun updateGamesScore(games: List<GameScoreUpdateData>) {
        dao.updateGamesScore(games)
    }
}
