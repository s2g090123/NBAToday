package com.jiachian.nbatoday.repository.data

import com.jiachian.nbatoday.datasource.local.player.PlayerLocalSource
import com.jiachian.nbatoday.datasource.remote.player.PlayerRemoteSource
import com.jiachian.nbatoday.models.remote.player.extensions.toPlayer
import com.jiachian.nbatoday.player.data.PlayerRepository
import com.jiachian.nbatoday.player.data.model.local.Player
import kotlinx.coroutines.flow.Flow

class TestPlayerRepository(
    private val playerLocalSource: PlayerLocalSource,
    private val playerRemoteSource: PlayerRemoteSource
) : PlayerRepository() {
    override suspend fun addPlayer(playerId: Int) {
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
    }

    override fun getPlayer(playerId: Int): Flow<Player?> {
        return playerLocalSource.getPlayer(playerId)
    }
}
