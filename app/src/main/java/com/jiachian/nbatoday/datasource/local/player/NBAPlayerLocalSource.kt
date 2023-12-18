package com.jiachian.nbatoday.datasource.local.player

import com.jiachian.nbatoday.database.dao.PlayerDao
import com.jiachian.nbatoday.models.local.player.Player
import kotlinx.coroutines.flow.Flow

class NBAPlayerLocalSource(
    private val playerDao: PlayerDao,
) : PlayerLocalSource() {
    override fun getPlayer(playerId: Int): Flow<Player?> {
        return playerDao.getPlayer(playerId)
    }

    override suspend fun insertPlayer(player: Player) {
        playerDao.insertPlayer(player)
    }
}
