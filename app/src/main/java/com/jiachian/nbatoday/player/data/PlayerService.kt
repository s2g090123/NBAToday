package com.jiachian.nbatoday.player.data

import com.jiachian.nbatoday.player.data.model.remote.PlayerDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerService {
    @GET("player/detail")
    suspend fun getPlayer(
        @Query("season") season: String,
        @Query("id") playerId: Int
    ): Response<PlayerDto>
}
