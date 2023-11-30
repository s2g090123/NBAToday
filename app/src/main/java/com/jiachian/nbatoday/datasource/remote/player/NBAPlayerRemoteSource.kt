package com.jiachian.nbatoday.datasource.remote.player

import com.jiachian.nbatoday.CurrentSeason
import com.jiachian.nbatoday.models.remote.player.RemotePlayer
import com.jiachian.nbatoday.service.PlayerService
import retrofit2.Response

class NBAPlayerRemoteSource : PlayerRemoteSource() {

    private val playerService by lazy {
        retrofit.create(PlayerService::class.java)
    }

    override suspend fun getPlayer(playerId: Int): Response<RemotePlayer> {
        return playerService.getPlayer(season = CurrentSeason, playerId = playerId)
    }
}
