package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.data.remote.user.LoginBody
import com.jiachian.nbatoday.data.remote.user.UpdatePasswordBody
import com.jiachian.nbatoday.data.remote.user.UpdatePointBody
import com.jiachian.nbatoday.data.remote.user.User
import retrofit2.http.*

interface NbaService {

    @GET("player/detail")
    suspend fun getPlayerDetail(
        @Query("season") season: String,
        @Query("id") playerId: Int
    ): RemotePlayerDetail?

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

    @GET("game/scoreboards")
    suspend fun getScoreboards(
        @Query("leagueID") leagueID: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("total") total: Int
    ): List<GameScoreboard>?

    @GET("game/schedule")
    suspend fun getSchedule(): Schedule?

    @GET("game/{gameId}")
    suspend fun getGameBoxScore(
        @Path("gameId") gameId: String
    ): RemoteGameBoxScore?

    /** User */
    @POST("user/login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): User?

    @POST("user/register")
    suspend fun register(
        @Body loginBody: LoginBody
    ): User?

    @POST("user/password")
    suspend fun updatePassword(
        @Body updatePasswordBody: UpdatePasswordBody
    ): String?

    @POST("user/points")
    suspend fun updatePoints(
        @Body updatePointBody: UpdatePointBody
    ): String?
}