package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.models.remote.player.RemotePlayer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerService {
    @GET("player/detail")
    suspend fun getPlayer(
        @Query("season") season: String,
        @Query("id") playerId: Int
    ): Response<RemotePlayer>
}
