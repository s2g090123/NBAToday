package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.datasource.local.player.PlayerLocalSource
import com.jiachian.nbatoday.models.local.player.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TestPlayerLocalSource(
    dataHolder: DataHolder
) : PlayerLocalSource() {
    private val players = dataHolder.players

    override fun getPlayer(playerId: Int): Flow<Player?> {
        return players.map { players ->
            players.firstOrNull { player ->
                player.playerId == playerId
            }
        }
    }

    override suspend fun insertPlayer(player: Player) {
        players.value = players.value.toMutableList().apply {
            removeIf { it.playerId == player.playerId }
            add(player)
        }
    }
}
