package com.jiachian.nbatoday.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.score.BoxScore
import kotlinx.coroutines.flow.Flow

@Dao
interface BoxScoreDao {
    @Query("SELECT * FROM score WHERE game_id == :gameId")
    fun getGameBoxScore(gameId: String): Flow<BoxScore?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameBoxScore(boxScore: BoxScore)

    @Update(entity = Game::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGamesScore(games: List<GameScoreUpdateData>)
}
