package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TestBoxScoreLocalSource(
    dataHolder: DataHolder,
) : BoxScoreLocalSource() {
    private val boxScores = dataHolder.boxScores
    private val boxScoresAndGames = dataHolder.boxScoresAndGames

    override fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?> {
        return boxScoresAndGames.map { boxScoresAndGames ->
            boxScoresAndGames.firstOrNull { boxScoreAndGame ->
                boxScoreAndGame.game.gameId == gameId
            }
        }
    }

    override suspend fun insertBoxScore(boxScore: BoxScore) {
        boxScores.value = boxScores.value.toMutableList().apply {
            removeIf { it.gameId == boxScore.gameId }
            add(boxScore)
        }
    }
}
