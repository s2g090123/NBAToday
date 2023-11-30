package com.jiachian.nbatoday.datasource.local.player

import com.jiachian.nbatoday.database.dao.PlayerDao
import com.jiachian.nbatoday.models.local.player.PlayerCareer
import com.jiachian.nbatoday.models.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.models.local.player.PlayerCareerStatsUpdate
import kotlinx.coroutines.flow.Flow

class NBAPlayerLocalSource(
    private val playerDao: PlayerDao,
) : PlayerLocalSource() {
    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return playerDao.getPlayers(playerId)
    }

    override suspend fun insertPlayerCareer(stats: PlayerCareer) {
        playerDao.insertPlayers(stats)
    }

    override suspend fun updatePlayerCareerInfo(info: PlayerCareerInfoUpdate) {
        playerDao.updatePlayerInfo(info)
    }

    override suspend fun updatePlayerCareerStats(stats: PlayerCareerStatsUpdate) {
        playerDao.updatePlayerStats(stats)
    }

    override suspend fun existsPlayer(playerId: Int): Boolean {
        return playerDao.exitsPlayer(playerId)
    }
}
