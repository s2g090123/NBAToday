package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import retrofit2.http.GET
import retrofit2.http.Query

interface StatsNbaService {
    @GET("scoreboardv3")
    suspend fun getScoreboard(
        @Query("LeagueId") leagueId: String,
        @Query("GameDate") gameDate: String
    ): GameScoreboard?

    @GET("leaguedashteamstats")
    suspend fun getTeamStats(
        @Query("Season") season: String,
        @Query("SeasonType") seasonType: String = "Regular Season",
        @Query("TeamID") teamId: Int = 0,
        @Query("MeasureType") measureType: String = "Base",
        @Query("Month") month: Int = 0,
        @Query("OpponentTeamID") opponentTeamID: Int = 0,
        @Query("PaceAdjust") paceAdjust: String = "N",
        @Query("PerMode") perMode: String = "Totals",
        @Query("Period") period: Int = 0,
        @Query("LastNGames") lastNGames: Int = 0,
        @Query("Conference") conference: String = "",
        @Query("DateFrom") dateFrom: String = "",
        @Query("DateTo") dateTo: String = "",
        @Query("Division") division: String = "",
        @Query("GameScope") gameScope: String? = "",
        @Query("GameSegment") gameSegment: String? = "",
        @Query("LeagueID") leagueId: String? = "",
        @Query("Location") location: String? = "",
        @Query("Outcome") outcome: String? = "",
        @Query("PORound") poRound: String? = "",
        @Query("PlayerExperience") playerExperience: String? = "",
        @Query("PlayerPosition") playerPosition: String? = "",
        @Query("PlusMinus") plusMinus: String = "N",
        @Query("Rank") rank: String = "N",
        @Query("SeasonSegment") seasonSegment: String? = "",
        @Query("ShotClockRange") shotClockRange: String? = "",
        @Query("StarterBench") starterBench: String? = "",
        @Query("TwoWay") twoWay: String? = "",
        @Query("VsConference") vsConference: String? = "",
        @Query("VsDivision") vaDivision: String? = ""
    ): RemoteTeamStats?
}