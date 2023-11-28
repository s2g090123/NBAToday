package com.jiachian.nbatoday.datasource.local.player

import com.jiachian.nbatoday.database.dao.PlayerDao
import com.jiachian.nbatoday.models.local.player.PlayerCareer
import com.jiachian.nbatoday.models.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.models.local.player.PlayerCareerStatsUpdate
import kotlinx.coroutines.flow.Flow

class NbaPlayerLocalSource(
    private val playerDao: PlayerDao,
) : PlayerLocalSource() {
    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return playerDao.getPlayerCareer(playerId)
    }

    override suspend fun insertPlayerCareer(stats: PlayerCareer) {
        playerDao.insertPlayerCareer(stats)
    }

    override suspend fun updatePlayerCareerInfo(info: PlayerCareerInfoUpdate) {
        playerDao.updatePlayerCareerInfo(info)
    }

    override suspend fun updatePlayerCareerStats(stats: PlayerCareerStatsUpdate) {
        playerDao.updatePlayerCareerStats(stats)
    }

    override suspend fun existPlayer(playerId: Int): Boolean {
        return playerDao.exitsPlayer(playerId)
    }
}
