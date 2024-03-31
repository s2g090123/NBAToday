package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.datasource.local.player.PlayerLocalSource
import com.jiachian.nbatoday.player.data.PlayerDao
import com.jiachian.nbatoday.player.data.model.local.Player
import kotlinx.coroutines.flow.Flow

class TestPlayerLocalSource(
    private val playerDao: PlayerDao
) : PlayerLocalSource() {
    override fun getPlayer(playerId: Int): Flow<Player?> {
        return playerDao.getPlayer(playerId)
    }

    override suspend fun insertPlayer(player: Player) {
        playerDao.addPlayer(player)
    }
}
