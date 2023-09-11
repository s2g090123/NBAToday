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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NbaService {

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

    @GET("team/{teamId}/players")
    suspend fun getTeamPlayersStats(
        @Path("teamId") teamId: Int,
        @Query("season") season: String
    ): Response<RemoteTeamPlayerStats>

    @GET("team/stats")
    suspend fun getTeamStats(
        @Query("season") season: String,
        @Query("id") teamId: Int
    ): Response<RemoteTeamStats>

    @GET("team/stats")
    suspend fun getTeamStats(
        @Query("season") season: String
    ): Response<RemoteTeamStats>

    @GET("game/scoreboard")
    suspend fun getScoreboard(
        @Query("leagueID") leagueID: String,
        @Query("gameDate") gameDate: String
    ): Response<GameScoreboard>

    @GET("game/scoreboards")
    suspend fun getScoreboards(
        @Query("leagueID") leagueID: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("total") total: Int
    ): Response<List<GameScoreboard>>

    @GET("game/schedule")
    suspend fun getSchedule(): Response<Schedule>

    @GET("game/{gameId}")
    suspend fun getGameBoxScore(
        @Path("gameId") gameId: String
    ): Response<RemoteGameBoxScore>

    /** User */
    @POST("user/login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): Response<User>

    @POST("user/register")
    suspend fun register(
        @Body loginBody: LoginBody
    ): Response<User>

    @POST("user/password")
    suspend fun updatePassword(
        @Body updatePasswordBody: UpdatePasswordBody
    ): Response<String>

    @POST("user/points")
    suspend fun updatePoints(
        @Body updatePointBody: UpdatePointBody
    ): Response<String>
}
