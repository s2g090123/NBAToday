package com.jiachian.nbatoday.datasource.remote.player

import com.jiachian.nbatoday.CurrentSeason
import com.jiachian.nbatoday.models.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.models.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.models.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.service.PlayerService
import retrofit2.Response

class NBAPlayerRemoteSource : PlayerRemoteSource() {

    private val playerService by lazy {
        retrofit.create(PlayerService::class.java)
    }

    override suspend fun getPlayerInfo(playerId: Int): Response<RemotePlayerInfo> {
        return playerService.getPlayerInfo(playerId)
    }

    override suspend fun getPlayerStats(playerId: Int): Response<RemotePlayerStats> {
        return playerService.getPlayerStats(season = CurrentSeason, playerId = playerId)
    }

    override suspend fun getPlayerDetail(playerId: Int): Response<RemotePlayerDetail> {
        return playerService.getPlayerDetail(season = CurrentSeason, playerId = playerId)
    }
}
