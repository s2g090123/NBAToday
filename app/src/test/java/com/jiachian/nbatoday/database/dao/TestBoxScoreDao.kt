package com.jiachian.nbatoday.database.dao

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TestBoxScoreDao(
    private val dataHolder: DataHolder
) : BoxScoreDao {
    override fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?> {
        return dataHolder.boxScoresAndGames.map { boxScoresAndGames ->
            boxScoresAndGames.firstOrNull { boxScoreAndGame ->
                boxScoreAndGame.game.gameId == gameId
            }
        }
    }

    override suspend fun insertBoxScore(boxScore: BoxScore) {
        dataHolder.boxScores.value = dataHolder.boxScores.value.toMutableList().apply {
            removeIf { it.gameId == boxScore.gameId }
            add(boxScore)
        }
    }
}
