package com.jiachian.nbatoday.data.repository.player

import com.jiachian.nbatoday.data.local.datasource.player.PlayerLocalSource
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.remote.datasource.player.PlayerRemoteSource
import kotlinx.coroutines.flow.Flow

class NbaPlayerRepository(
    private val playerLocalSource: PlayerLocalSource,
    private val playerRemoteSource: PlayerRemoteSource,
) : PlayerRepository() {
    override suspend fun refreshPlayerStats(playerId: Int) {
        val detail = playerRemoteSource.getPlayerDetail(playerId)
        val info = detail?.info
        val stats = detail?.stats
        if (playerLocalSource.existPlayer(playerId)) {
            info?.toUpdateData()?.also {
                playerLocalSource.updatePlayerCareerInfo(it)
            }
            stats?.toLocal()?.also {
                playerLocalSource.updatePlayerCareerStats(it)
            }
        } else if (info != null && stats != null) {
            val infoData = info.toUpdateData()?.info
            val statsData = stats.toLocal()?.stats
            if (infoData != null && statsData != null) {
                playerLocalSource.insertPlayerCareer(
                    PlayerCareer(playerId, infoData, statsData)
                )
            }
        }
    }

    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return playerLocalSource.getPlayerCareer(playerId)
    }
}
