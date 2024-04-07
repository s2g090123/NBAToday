package com.jiachian.nbatoday.database.dao

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.player.data.PlayerDao
import com.jiachian.nbatoday.player.data.model.local.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TestPlayerDao(
    private val dataHolder: DataHolder,
) : PlayerDao {
    override fun getPlayer(playerId: Int): Flow<Player?> {
        return dataHolder.players.map { players ->
            players.firstOrNull { player ->
                player.playerId == playerId
            }
        }
    }

    override suspend fun addPlayer(player: Player) {
        dataHolder.players.value = dataHolder.players.value.toMutableList().apply {
            removeIf { it.playerId == player.playerId }
            add(player)
        }
    }
}
