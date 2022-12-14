package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import retrofit2.http.GET
import retrofit2.http.Query

interface StatsNbaService {
    @GET("stats/scoreboardv3")
    suspend fun getScoreboard(
        @Query("LeagueId") leagueId: String,
        @Query("GameDate") gameDate: String
    ): GameScoreboard?
}