package com.jiachian.nbatoday.repository.player

import com.jiachian.nbatoday.datasource.local.player.PlayerLocalSource
import com.jiachian.nbatoday.datasource.remote.player.PlayerRemoteSource
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.remote.player.extensions.toPlayer
import kotlinx.coroutines.flow.Flow

class NBAPlayerRepository(
    private val playerLocalSource: PlayerLocalSource,
    private val playerRemoteSource: PlayerRemoteSource,
) : PlayerRepository() {
    override suspend fun insertPlayer(playerId: Int) {
        loading {
            playerRemoteSource
                .getPlayer(playerId)
                .takeIf { !it.isError() }
                ?.body()
                ?.toPlayer()
                ?.also { player ->
                    playerLocalSource.insertPlayer(player)
                }
                ?: onError()
        }
    }

    override fun getPlayer(playerId: Int): Flow<Player?> {
        return playerLocalSource.getPlayer(playerId)
    }
}
