package com.jiachian.nbatoday.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT EXISTS (SELECT 1 FROM nba_player_career_stats WHERE person_id == :playerId)")
    fun exitsPlayer(playerId: Int): Boolean

    @Query("SELECT * FROM nba_player_career_stats WHERE person_id == :playerId")
    fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerCareer(stats: PlayerCareer)

    @Update(entity = PlayerCareer::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayerCareerInfo(info: PlayerCareerInfoUpdate)

    @Update(entity = PlayerCareer::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayerCareerStats(stats: PlayerCareerStatsUpdate)
}
