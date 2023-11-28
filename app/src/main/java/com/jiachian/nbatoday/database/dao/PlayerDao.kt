package com.jiachian.nbatoday.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jiachian.nbatoday.models.local.player.PlayerCareer
import com.jiachian.nbatoday.models.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.models.local.player.PlayerCareerStatsUpdate
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT EXISTS (SELECT 1 FROM player WHERE player_id == :playerId)")
    fun exitsPlayer(playerId: Int): Boolean

    @Query("SELECT * FROM player WHERE player_id == :playerId")
    fun getPlayers(playerId: Int): Flow<PlayerCareer?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(stats: PlayerCareer)

    @Update(entity = PlayerCareer::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayerInfo(info: PlayerCareerInfoUpdate)

    @Update(entity = PlayerCareer::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayerStats(stats: PlayerCareerStatsUpdate)
}
