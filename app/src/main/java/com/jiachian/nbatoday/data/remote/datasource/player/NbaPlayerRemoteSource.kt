package com.jiachian.nbatoday.data.remote.datasource.player

import com.jiachian.nbatoday.CURRENT_SEASON
import com.jiachian.nbatoday.data.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.service.PlayerService

class NbaPlayerRemoteSource : PlayerRemoteSource() {

    private val playerService by lazy {
        retrofit.create(PlayerService::class.java)
    }

    override suspend fun getPlayerInfo(playerId: Int): RemotePlayerInfo? {
        return playerService.getPlayerInfo(playerId).body()
    }

    override suspend fun getPlayerCareerStats(playerId: Int): RemotePlayerStats? {
        return playerService.getPlayerStats(season = CURRENT_SEASON, playerId = playerId).body()
    }

    override suspend fun getPlayerDetail(playerId: Int): RemotePlayerDetail? {
        return playerService.getPlayerDetail(season = CURRENT_SEASON, playerId = playerId).body()
    }
}
