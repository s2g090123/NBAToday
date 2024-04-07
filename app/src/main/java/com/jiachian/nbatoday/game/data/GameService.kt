package com.jiachian.nbatoday.game.data

import com.jiachian.nbatoday.boxscore.data.model.remote.BoxScoreDto
import com.jiachian.nbatoday.game.data.model.remote.GameDto
import com.jiachian.nbatoday.game.data.model.remote.ScheduleDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameService {
    @GET("game/scoreboard")
    suspend fun getGame(
        @Query("leagueID") leagueID: String,
        @Query("gameDate") gameDate: String
    ): Response<GameDto>

    @GET("game/scoreboards")
    suspend fun getGames(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("total") total: Int
    ): Response<List<GameDto>>

    @GET("game/schedule")
    suspend fun getSchedule(): Response<ScheduleDto>

    @GET("game/{gameId}")
    suspend fun getBoxScore(
        @Path("gameId") gameId: String
    ): Response<BoxScoreDto>
}
