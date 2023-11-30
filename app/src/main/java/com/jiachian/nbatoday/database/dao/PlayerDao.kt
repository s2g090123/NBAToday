package com.jiachian.nbatoday.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiachian.nbatoday.models.local.player.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player WHERE player_id == :playerId")
    fun getPlayers(playerId: Int): Flow<Player?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(stats: Player)
}
