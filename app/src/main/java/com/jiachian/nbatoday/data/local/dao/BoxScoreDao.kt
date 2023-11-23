package com.jiachian.nbatoday.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
import kotlinx.coroutines.flow.Flow

@Dao
interface BoxScoreDao {
    @Update(entity = NbaGame::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGamesScore(games: List<GameScoreUpdateData>)

    @Query("SELECT * FROM nba_game_box_score WHERE game_id == :gameId")
    fun getGameBoxScore(gameId: String): Flow<GameBoxScore?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameBoxScore(boxScore: GameBoxScore)
}
