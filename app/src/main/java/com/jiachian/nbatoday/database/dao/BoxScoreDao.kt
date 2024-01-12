package com.jiachian.nbatoday.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import kotlinx.coroutines.flow.Flow

/**
 * Handle operations related to the BoxScore entity in the database.
 */
@Dao
interface BoxScoreDao {
    /**
     * Retrieves a flow of BoxScoreAndGame for a specific game ID from the database.
     *
     * @param gameId The ID of the game for which to retrieve the BoxScoreAndGame.
     * @return Flow emitting a nullable [BoxScoreAndGame].
     */
    @Query("SELECT * FROM score WHERE game_id == :gameId")
    fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?>

    /**
     * Inserts or replaces a BoxScore entry in the database.
     *
     * @param boxScore The [BoxScore] object to be inserted or replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoxScore(boxScore: BoxScore)
}
