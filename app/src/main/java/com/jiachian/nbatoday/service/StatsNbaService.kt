package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.models.remote.game.RemoteGame
import com.jiachian.nbatoday.models.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.models.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayerStats
import com.jiachian.nbatoday.models.remote.team.RemoteTeamStats
import retrofit2.http.GET
import retrofit2.http.Query

@Deprecated("Replace with NbaService", ReplaceWith("NbaService"))
interface StatsNbaService {
    @GET("scoreboardv3")
    suspend fun getScoreboard(
        @Query("LeagueId") leagueId: String,
        @Query("GameDate") gameDate: String
    ): RemoteGame?

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
        @Query("Location") location: String? = "",
        @Query("Outcome") outcome: String? = "",
        @Query("PORound") poRound: String? = "",
        @Query("PlayerExperience") playerExperience: String? = "",
        @Query("PlayerPosition") playerPosition: String? = "",
        @Query("PlusMinus") plusMinus: String = "N",
        @Query("Rank") rank: String = "N",
        @Query("SeasonSegment") seasonSegment: String? = "",
        @Query("StarterBench") starterBench: String? = "",
        @Query("TwoWay") twoWay: String? = "",
        @Query("VsConference") vsConference: String? = "",
        @Query("VsDivision") vaDivision: String? = ""
    ): RemoteTeamStats?

    @GET("teamplayerdashboard")
    suspend fun getTeamPlayerStats(
        @Query("Season") season: String,
        @Query("SeasonType") seasonType: String = "Regular Season",
        @Query("TeamID") teamId: Int,
        @Query("MeasureType") measureType: String = "Base",
        @Query("Month") month: Int = 0,
        @Query("OpponentTeamID") opponentTeamID: Int = 0,
        @Query("PaceAdjust") paceAdjust: String = "N",
        @Query("PerMode") perMode: String = "Totals",
        @Query("Period") period: Int = 0,
        @Query("LastNGames") lastNGames: Int = 0,
        @Query("DateFrom") dateFrom: String = "",
        @Query("DateTo") dateTo: String = "",
        @Query("GameSegment") gameSegment: String? = "",
        @Query("Location") location: String? = "",
        @Query("Outcome") outcome: String? = "",
        @Query("PORound") poRound: String? = "",
        @Query("PlusMinus") plusMinus: String = "N",
        @Query("Rank") rank: String = "N",
        @Query("SeasonSegment") seasonSegment: String? = "",
        @Query("VsConference") vsConference: String? = "",
        @Query("VsDivision") vaDivision: String? = ""
    ): RemoteTeamPlayerStats?

    @GET("commonplayerinfo")
    suspend fun getPlayerInfo(
        @Query("PlayerID") playerId: Int
    ): RemotePlayerInfo?

    @GET("playerdashboardbyyearoveryear")
    suspend fun getPlayerCareerStats(
        @Query("Season") season: String,
        @Query("SeasonType") seasonType: String = "Regular Season",
        @Query("PlayerID") playerId: Int,
        @Query("MeasureType") measureType: String = "Base",
        @Query("Month") month: Int = 0,
        @Query("OpponentTeamID") opponentTeamID: Int = 0,
        @Query("PaceAdjust") paceAdjust: String = "N",
        @Query("PerMode") perMode: String = "Totals",
        @Query("Period") period: Int = 0,
        @Query("LastNGames") lastNGames: Int = 0,
        @Query("DateFrom") dateFrom: String = "",
        @Query("DateTo") dateTo: String = "",
        @Query("GameSegment") gameSegment: String? = "",
        @Query("Location") location: String? = "",
        @Query("Outcome") outcome: String? = "",
        @Query("PlusMinus") plusMinus: String = "N",
        @Query("Rank") rank: String = "Y",
        @Query("SeasonSegment") seasonSegment: String? = "",
        @Query("VsConference") vsConference: String? = "",
        @Query("VsDivision") vaDivision: String? = ""
    ): RemotePlayerStats?
}
