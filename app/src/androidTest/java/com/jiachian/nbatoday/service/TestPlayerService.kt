package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.RemotePlayerGenerator
import com.jiachian.nbatoday.player.data.PlayerService
import com.jiachian.nbatoday.player.data.model.remote.PlayerDto
import retrofit2.Response

class TestPlayerService : PlayerService {
    override suspend fun getPlayer(season: String, playerId: Int): Response<PlayerDto> {
        return Response.success(RemotePlayerGenerator.get(playerId))
    }
}
