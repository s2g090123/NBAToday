package com.jiachian.nbatoday.datasource.remote.data

import com.jiachian.nbatoday.data.remote.RemotePlayerGenerator
import com.jiachian.nbatoday.datasource.remote.player.PlayerRemoteSource
import com.jiachian.nbatoday.player.data.model.remote.PlayerDto
import retrofit2.Response

class TestPlayerRemoteSource : PlayerRemoteSource() {
    override suspend fun getPlayer(playerId: Int): Response<PlayerDto> {
        return Response.success(RemotePlayerGenerator.get(playerId))
    }
}
