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
            val response = playerRemoteSource.getPlayer(playerId)
            if (response.isError()) {
                showErrorToast()
                return@loading
            }
            response
                .body()
                ?.also { remotePlayer ->
                    val player = remotePlayer.toPlayer() ?: run {
                        showErrorToast()
                        return@also
                    }
                    playerLocalSource.insertPlayer(player)
                }
        }
    }

    override fun getPlayer(playerId: Int): Flow<Player?> {
        return playerLocalSource.getPlayer(playerId)
    }
}
