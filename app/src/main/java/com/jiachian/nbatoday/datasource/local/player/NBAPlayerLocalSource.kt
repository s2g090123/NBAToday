package com.jiachian.nbatoday.datasource.local.player

import com.jiachian.nbatoday.database.dao.PlayerDao
import com.jiachian.nbatoday.models.local.player.PlayerCareer
import kotlinx.coroutines.flow.Flow

class NBAPlayerLocalSource(
    private val playerDao: PlayerDao,
) : PlayerLocalSource() {
    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return playerDao.getPlayers(playerId)
    }

    override suspend fun insertPlayerCareer(playerCareer: PlayerCareer) {
        playerDao.insertPlayers(playerCareer)
    }
}
