package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameService {
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
}
