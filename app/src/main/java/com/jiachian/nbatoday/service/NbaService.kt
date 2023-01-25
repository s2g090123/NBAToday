package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NbaService {

    @GET("player/stats")
    suspend fun getPlayerStats(
        @Query("season") season: String,
        @Query("id") playerId: Int
    ): RemotePlayerStats?

    @GET("player/info")
    suspend fun getPlayerInfo(
        @Query("id") playerId: Int
    ): RemotePlayerInfo?

    @GET("team/{teamId}/players")
    suspend fun getTeamPlayersStats(
        @Path("teamId") teamId: Int,
        @Query("season") season: String
    ): RemoteTeamPlayerStats?

    @GET("team/stats")
    suspend fun getTeamStats(
        @Query("season") season: String,
        @Query("id") teamId: Int
    ): RemoteTeamStats?

    @GET("team/stats")
    suspend fun getTeamStats(
        @Query("season") season: String
    ): RemoteTeamStats?

    @GET("game/scoreboard")
    suspend fun getScoreboard(
        @Query("leagueID") leagueID: String,
        @Query("gameDate") gameDate: String
    ): GameScoreboard?

    @GET("game/schedule")
    suspend fun getSchedule(): Schedule?

    @GET("game/{gameId}")
    suspend fun getGameBoxScore(
        @Path("gameId") gameId: String
    ): RemoteGameBoxScore?
}