package com.jiachian.nbatoday.datasource.local.player

import com.jiachian.nbatoday.models.local.player.Player
import kotlinx.coroutines.flow.Flow

/**
 * The local data source for player-related operations.
 */
abstract class PlayerLocalSource {
    /**
     * Retrieves a [Flow] of [Player] representing a player with the specified player ID.
     *
     * @param playerId The ID of the player.
     * @return A [Flow] emitting a nullable [Player] object.
     */
    abstract fun getPlayer(playerId: Int): Flow<Player?>

    /**
     * Inserts a [Player] object into the local data source.
     *
     * @param player The [Player] object to be inserted.
     */
    abstract suspend fun insertPlayer(player: Player)
}
