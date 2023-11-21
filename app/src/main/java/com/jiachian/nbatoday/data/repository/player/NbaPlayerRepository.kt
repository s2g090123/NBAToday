package com.jiachian.nbatoday.data.repository.player

import com.jiachian.nbatoday.data.local.LocalDataSource
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class NbaPlayerRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : PlayerRepository() {
    override suspend fun refreshPlayerStats(playerId: Int) {
        val detail = remoteDataSource.getPlayerDetail(playerId)
        val info = detail?.info
        val stats = detail?.stats
        if (localDataSource.existPlayer(playerId)) {
            info?.toUpdateData()?.also {
                localDataSource.updatePlayerCareerInfo(it)
            }
            stats?.toLocal()?.also {
                localDataSource.updatePlayerCareerStats(it)
            }
        } else if (info != null && stats != null) {
            val infoData = info.toUpdateData()?.info
            val statsData = stats.toLocal()?.stats
            if (infoData != null && statsData != null) {
                localDataSource.insertPlayerCareer(
                    PlayerCareer(playerId, infoData, statsData)
                )
            }
        }
    }

    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return localDataSource.getPlayerCareer(playerId)
    }
}
