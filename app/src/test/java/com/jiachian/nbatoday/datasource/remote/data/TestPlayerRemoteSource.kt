package com.jiachian.nbatoday.datasource.remote.data

import com.jiachian.nbatoday.data.remote.RemotePlayerGenerator
import com.jiachian.nbatoday.datasource.remote.player.PlayerRemoteSource
import com.jiachian.nbatoday.models.remote.player.RemotePlayer
import retrofit2.Response

class TestPlayerRemoteSource : PlayerRemoteSource() {
    override suspend fun getPlayer(playerId: Int): Response<RemotePlayer> {
        return Response.success(RemotePlayerGenerator.get(playerId))
    }
}
