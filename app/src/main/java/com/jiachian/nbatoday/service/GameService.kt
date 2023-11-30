package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.models.remote.game.RemoteGame
import com.jiachian.nbatoday.models.remote.game.RemoteSchedule
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameService {
    @GET("game/scoreboard")
    suspend fun getGame(
        @Query("leagueID") leagueID: String,
        @Query("gameDate") gameDate: String
    ): Response<RemoteGame>

    @GET("game/scoreboards")
    suspend fun getGames(
        @Query("leagueID") leagueID: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("total") total: Int
    ): Response<List<RemoteGame>>

    @GET("game/schedule")
    suspend fun getSchedule(): Response<RemoteSchedule>

    @GET("game/{gameId}")
    suspend fun getBoxScore(
        @Path("gameId") gameId: String
    ): Response<RemoteBoxScore>
}
