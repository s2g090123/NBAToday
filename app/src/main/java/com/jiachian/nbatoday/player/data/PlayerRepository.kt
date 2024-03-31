package com.jiachian.nbatoday.player.data

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.player.data.model.local.Player
import kotlinx.coroutines.flow.Flow

/**
 * Manage player-related data.
 */
interface PlayerRepository {
    /**
     * Inserts a player with the specified player ID.
     *
     * @param playerId The ID of the player to be inserted.
     */
    suspend fun addPlayer(playerId: Int): Response<Unit>

    /**
     * Retrieves a flow of player information for a specific player ID.
     *
     * @param playerId The ID of the player for which to retrieve information.
     * @return A Flow emitting a Player object, or null if no information is available.
     */
    fun getPlayer(playerId: Int): Flow<Player?>
}
