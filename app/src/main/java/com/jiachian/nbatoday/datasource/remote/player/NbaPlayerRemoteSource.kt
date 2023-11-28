package com.jiachian.nbatoday.datasource.remote.player

import com.jiachian.nbatoday.CurrentSeason
import com.jiachian.nbatoday.models.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.models.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.models.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.service.PlayerService

class NbaPlayerRemoteSource : PlayerRemoteSource() {

    private val playerService by lazy {
        retrofit.create(PlayerService::class.java)
    }

    override suspend fun getPlayerInfo(playerId: Int): RemotePlayerInfo? {
        return playerService.getPlayerInfo(playerId).body()
    }

    override suspend fun getPlayerCareerStats(playerId: Int): RemotePlayerStats? {
        return playerService.getPlayerStats(season = CurrentSeason, playerId = playerId).body()
    }

    override suspend fun getPlayerDetail(playerId: Int): RemotePlayerDetail? {
        return playerService.getPlayerDetail(season = CurrentSeason, playerId = playerId).body()
    }
}
