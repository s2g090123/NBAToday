package com.jiachian.nbatoday.datasource.local.boxscore

import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import kotlinx.coroutines.flow.Flow

/**
 * The local data source for box score-related operations.
 */
abstract class BoxScoreLocalSource {
    /**
     * Retrieves a flow of [BoxScoreAndGame] objects associated with a specific game ID.
     *
     * @param gameId The ID of the game for which to retrieve the box score and game.
     * @return A [Flow] emitting a nullable [BoxScoreAndGame] object.
     */
    abstract fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?>

    /**
     * Inserts a [BoxScore] object into the local data source.
     *
     * @param boxScore The [BoxScore] object to be inserted.
     */
    abstract suspend fun insertBoxScore(boxScore: BoxScore)
}
