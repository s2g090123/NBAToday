package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.models.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.models.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.models.remote.player.RemotePlayerStats
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerService {
    @GET("player/detail")
    suspend fun getPlayerDetail(
        @Query("season") season: String,
        @Query("id") playerId: Int
    ): Response<RemotePlayerDetail>

    @GET("player/stats")
    suspend fun getPlayerStats(
        @Query("season") season: String,
        @Query("id") playerId: Int
    ): Response<RemotePlayerStats>

    @GET("player/info")
    suspend fun getPlayerInfo(
        @Query("id") playerId: Int
    ): Response<RemotePlayerInfo>
}
