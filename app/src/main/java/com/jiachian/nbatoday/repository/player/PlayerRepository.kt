package com.jiachian.nbatoday.repository.player

import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

/**
 * Manage player-related data.
 */
abstract class PlayerRepository : BaseRepository() {
    /**
     * Inserts a player with the specified player ID.
     *
     * @param playerId The ID of the player to be inserted.
     */
    abstract suspend fun addPlayer(playerId: Int): Response<Unit>

    /**
     * Retrieves a flow of player information for a specific player ID.
     *
     * @param playerId The ID of the player for which to retrieve information.
     * @return A Flow emitting a Player object, or null if no information is available.
     */
    abstract fun getPlayer(playerId: Int): Flow<Player?>
}
