package com.jiachian.nbatoday.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiachian.nbatoday.models.local.player.Player
import kotlinx.coroutines.flow.Flow

/**
 * Handle operations related to the Player entity in the database.
 */
@Dao
interface PlayerDao {
    /**
     * Retrieves a flow of a player with a specific player ID from the database.
     *
     * @param playerId The ID of the player to retrieve.
     * @return Flow emitting a nullable [Player].
     */
    @Query("SELECT * FROM player WHERE player_id == :playerId")
    fun getPlayer(playerId: Int): Flow<Player?>

    /**
     * Inserts a player into the database, replacing any existing entry on conflict.
     *
     * @param player The [Player] object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayer(player: Player)
}
