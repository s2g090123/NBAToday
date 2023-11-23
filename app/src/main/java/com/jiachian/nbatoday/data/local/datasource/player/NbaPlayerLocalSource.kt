package com.jiachian.nbatoday.data.local.datasource.player

import com.jiachian.nbatoday.data.local.NbaDao
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate
import kotlinx.coroutines.flow.Flow

class NbaPlayerLocalSource(
    private val dao: NbaDao,
) : PlayerLocalSource() {
    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return dao.getPlayerCareer(playerId)
    }

    override suspend fun insertPlayerCareer(stats: PlayerCareer) {
        dao.insertPlayerCareer(stats)
    }

    override suspend fun updatePlayerCareerInfo(info: PlayerCareerInfoUpdate) {
        dao.updatePlayerCareerInfo(info)
    }

    override suspend fun updatePlayerCareerStats(stats: PlayerCareerStatsUpdate) {
        dao.updatePlayerCareerStats(stats)
    }

    override suspend fun existPlayer(playerId: Int): Boolean {
        return dao.exitsPlayer(playerId)
    }
}
