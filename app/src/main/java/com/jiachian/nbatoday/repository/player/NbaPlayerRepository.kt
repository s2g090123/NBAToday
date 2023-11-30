package com.jiachian.nbatoday.repository.player

import com.jiachian.nbatoday.datasource.local.player.PlayerLocalSource
import com.jiachian.nbatoday.datasource.remote.player.PlayerRemoteSource
import com.jiachian.nbatoday.models.local.player.PlayerCareer
import com.jiachian.nbatoday.models.remote.player.toPlayerCareerInfoUpdate
import com.jiachian.nbatoday.models.remote.player.toPlayerCareerStatsUpdate
import kotlinx.coroutines.flow.Flow

class NbaPlayerRepository(
    private val playerLocalSource: PlayerLocalSource,
    private val playerRemoteSource: PlayerRemoteSource,
) : PlayerRepository() {
    override suspend fun refreshPlayerStats(playerId: Int) {
        val detail = playerRemoteSource.getPlayerDetail(playerId)
        val info = detail?.info
        val stats = detail?.stats
        if (playerLocalSource.existsPlayer(playerId)) {
            info?.toPlayerCareerInfoUpdate()?.also {
                playerLocalSource.updatePlayerCareerInfo(it)
            }
            stats?.toPlayerCareerStatsUpdate()?.also {
                playerLocalSource.updatePlayerCareerStats(it)
            }
        } else if (info != null && stats != null) {
            val infoData = info.toPlayerCareerInfoUpdate()?.info
            val statsData = stats.toPlayerCareerStatsUpdate()?.stats
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
