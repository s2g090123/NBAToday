package com.jiachian.nbatoday.datasource.remote.player

import com.jiachian.nbatoday.models.remote.player.RemotePlayer
import retrofit2.Response

/**
 * The remote data source for player-related operations.
 */
abstract class PlayerRemoteSource {
    /**
     * Retrieves information about a specific NBA player from the remote data source.
     *
     * @param playerId The ID of the NBA player.
     * @return A [Response] containing a [RemotePlayer] object.
     */
    abstract suspend fun getPlayer(playerId: Int): Response<RemotePlayer>
}
