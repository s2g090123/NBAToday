package com.jiachian.nbatoday.data.remote.team

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.OneHundredPercentage
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.utils.getOrZero

data class RemoteTeamStats(
    @SerializedName("resultSets") val data: List<Result>?
) {
    data class Result(
        @SerializedName("name") val name: String?,
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    @Volatile
    private var resultImp: Result? = null

    private val result: Result?
        @Synchronized
        get() {
            return resultImp ?: data?.firstOrNull {
                it.name == "LeagueDashTeamStats"
            }?.also {
                resultImp = it
            }
        }

    fun toLocal(): List<TeamStats> {
        val rowData = result?.rowData ?: return emptyList()
        return rowData.mapNotNull { data ->
            createTeamStats(data) ?: return@mapNotNull null
        }
    }

    private fun getStatsResult(data: List<String>, name: String): String? {
        val headers = result?.headers ?: return null
        return data.getOrNull(headers.indexOf(name))
    }

    private fun createTeamStats(data: List<String>): TeamStats? {
        val teamId = getStatsResult(data, "TEAM_ID")?.toIntOrNull() ?: return null
        val team = NBATeam.getTeamById(teamId) ?: teamOfficial
        return TeamStats(
            teamId = teamId,
            team = team,
            teamConference = team.conference,
            gamePlayed = getStatsResult(data, "GP")?.toIntOrNull().getOrZero(),
            win = getStatsResult(data, "W")?.toIntOrNull().getOrZero(),
            lose = getStatsResult(data, "L")?.toIntOrNull().getOrZero(),
            winPercentage = getStatsResult(data, "W_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
            fieldGoalsMade = getStatsResult(data, "FGM")?.toIntOrNull().getOrZero(),
            fieldGoalsAttempted = getStatsResult(data, "FGA")?.toIntOrNull().getOrZero(),
            fieldGoalsPercentage = getStatsResult(data, "FG_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
            threePointersMade = getStatsResult(data, "FG3M")?.toIntOrNull().getOrZero(),
            threePointersAttempted = getStatsResult(data, "FG3A")?.toIntOrNull().getOrZero(),
            threePointersPercentage = getStatsResult(data, "FG3_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
            freeThrowsMade = getStatsResult(data, "FTM")?.toIntOrNull().getOrZero(),
            freeThrowsAttempted = getStatsResult(data, "FTA")?.toIntOrNull().getOrZero(),
            freeThrowsPercentage = getStatsResult(data, "FT_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
            reboundsOffensive = getStatsResult(data, "OREB")?.toIntOrNull().getOrZero(),
            reboundsDefensive = getStatsResult(data, "DREB")?.toIntOrNull().getOrZero(),
            reboundsTotal = getStatsResult(data, "REB")?.toIntOrNull().getOrZero(),
            assists = getStatsResult(data, "AST")?.toIntOrNull().getOrZero(),
            turnovers = getStatsResult(data, "TOV")?.toDoubleOrNull()?.toInt().getOrZero(),
            steals = getStatsResult(data, "STL")?.toIntOrNull().getOrZero(),
            blocks = getStatsResult(data, "BLK")?.toIntOrNull().getOrZero(),
            foulsPersonal = getStatsResult(data, "PF")?.toIntOrNull().getOrZero(),
            points = getStatsResult(data, "PTS")?.toIntOrNull().getOrZero(),
            plusMinus = getStatsResult(data, "PLUS_MINUS")?.toDoubleOrNull()?.toInt().getOrZero()
        )
    }

    private fun Double.toPercentage(): Double = this * OneHundredPercentage
}
