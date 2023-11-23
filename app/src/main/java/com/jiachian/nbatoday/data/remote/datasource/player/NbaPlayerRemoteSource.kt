package com.jiachian.nbatoday.data.remote.datasource.player

import com.jiachian.nbatoday.CURRENT_SEASON
import com.jiachian.nbatoday.data.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.service.ServiceProvider

class NbaPlayerRemoteSource : PlayerRemoteSource() {

    private val nbaService = ServiceProvider.nbaService

    override suspend fun getPlayerInfo(playerId: Int): RemotePlayerInfo? {
        return nbaService.getPlayerInfo(playerId).body()
    }

    override suspend fun getPlayerCareerStats(playerId: Int): RemotePlayerStats? {
        return nbaService.getPlayerStats(season = CURRENT_SEASON, playerId = playerId).body()
    }

    override suspend fun getPlayerDetail(playerId: Int): RemotePlayerDetail? {
        return nbaService.getPlayerDetail(season = CURRENT_SEASON, playerId = playerId).body()
    }
}
