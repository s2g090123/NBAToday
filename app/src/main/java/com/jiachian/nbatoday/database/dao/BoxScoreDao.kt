package com.jiachian.nbatoday.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import kotlinx.coroutines.flow.Flow

@Dao
interface BoxScoreDao {
    @Query("SELECT * FROM score WHERE game_id == :gameId")
    fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoxScore(boxScore: BoxScore)
}
