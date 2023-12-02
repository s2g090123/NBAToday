package com.jiachian.nbatoday.repository.player

import com.jiachian.nbatoday.datasource.local.player.PlayerLocalSource
import com.jiachian.nbatoday.datasource.remote.player.PlayerRemoteSource
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.remote.player.toPlayer
import com.jiachian.nbatoday.utils.showErrorToast
import kotlinx.coroutines.flow.Flow

class NBAPlayerRepository(
    private val playerLocalSource: PlayerLocalSource,
    private val playerRemoteSource: PlayerRemoteSource,
) : PlayerRepository() {
    override suspend fun updatePlayer(playerId: Int) {
        loading {
            playerRemoteSource
                .getPlayer(playerId)
                .takeIf { !it.isError() }
                ?.body()
                ?.let { remotePlayer ->
                    remotePlayer
                        .toPlayer()
                        ?.let { player ->
                            playerLocalSource.insertPlayer(player)
                        }
                }
                ?: showErrorToast()
        }
    }

    override fun getPlayer(playerId: Int): Flow<Player?> {
        return playerLocalSource.getPlayer(playerId)
    }
}
