package com.jiachian.nbatoday.data.remote.player

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.OneHundredPercentage
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero

data class RemotePlayerStats(
    @SerializedName("parameters") val parameters: Parameter?,
    @SerializedName("resultSets") val resultSets: List<Result>?
) {
    data class Parameter(
        @SerializedName("PlayerID") val playerId: Int?,
    )

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
            return resultImp ?: resultSets?.find {
                it.name == "ByYearPlayerDashboard"
            }?.also {
                resultImp = it
            }
        }

    fun toLocal(): PlayerCareerStatsUpdate? {
        val playerId = parameters?.playerId ?: return null
        val stats = arrayListOf<PlayerCareer.PlayerCareerStats.Stats>()
        val ranks = arrayListOf<PlayerCareer.PlayerCareerStats.Rank>()
        result?.rowData?.forEach { data ->
            val careerStats = createPlayerCareerStats(data) ?: return@forEach
            val careerRank = createPlayerCareerRank(data) ?: return@forEach
            stats.add(careerStats)
            ranks.add(careerRank)
        }
        return PlayerCareerStatsUpdate(
            personId = playerId,
            stats = PlayerCareer.PlayerCareerStats(stats, ranks)
        )
    }

    private fun getPlayerResult(data: List<String>, name: String): String? {
        val headers = result?.headers ?: return null
        return data.getOrNull(headers.indexOf(name))
    }

    private fun createPlayerCareerStats(data: List<String>): PlayerCareer.PlayerCareerStats.Stats? {
        return PlayerCareer.PlayerCareerStats.Stats(
            timeFrame = getPlayerResult(data, "GROUP_VALUE").getOrNA(),
            teamId = getPlayerResult(data, "TEAM_ID")?.toIntOrNull() ?: return null,
            teamNameAbbr = getPlayerResult(data, "TEAM_ABBREVIATION").getOrNA(),
            gamePlayed = getPlayerResult(data, "GP")?.toIntOrNull().getOrZero(),
            win = getPlayerResult(data, "W")?.toIntOrNull().getOrZero(),
            lose = getPlayerResult(data, "L")?.toIntOrNull().getOrZero(),
            winPercentage = getPlayerResult(data, "W_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
            fieldGoalsMade = getPlayerResult(data, "FGM")?.toIntOrNull().getOrZero(),
            fieldGoalsAttempted = getPlayerResult(data, "FGA")?.toIntOrNull().getOrZero(),
            fieldGoalsPercentage = getPlayerResult(data, "FG_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
            threePointersMade = getPlayerResult(data, "FG3M")?.toIntOrNull().getOrZero(),
            threePointersAttempted = getPlayerResult(data, "FG3A")?.toIntOrNull().getOrZero(),
            threePointersPercentage = getPlayerResult(data, "FG3_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
            freeThrowsMade = getPlayerResult(data, "FTM")?.toIntOrNull().getOrZero(),
            freeThrowsAttempted = getPlayerResult(data, "FTA")?.toIntOrNull().getOrZero(),
            freeThrowsPercentage = getPlayerResult(data, "FT_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
            reboundsOffensive = getPlayerResult(data, "OREB")?.toIntOrNull().getOrZero(),
            reboundsDefensive = getPlayerResult(data, "DREB")?.toIntOrNull().getOrZero(),
            reboundsTotal = getPlayerResult(data, "REB")?.toIntOrNull().getOrZero(),
            assists = getPlayerResult(data, "AST")?.toIntOrNull().getOrZero(),
            turnovers = getPlayerResult(data, "TOV")?.toIntOrNull().getOrZero(),
            steals = getPlayerResult(data, "STL")?.toIntOrNull().getOrZero(),
            blocks = getPlayerResult(data, "BLK")?.toIntOrNull().getOrZero(),
            foulsPersonal = getPlayerResult(data, "PF")?.toIntOrNull().getOrZero(),
            points = getPlayerResult(data, "PTS")?.toIntOrNull().getOrZero(),
            plusMinus = getPlayerResult(data, "PLUS_MINUS")?.toIntOrNull().getOrZero(),
        )
    }

    private fun createPlayerCareerRank(data: List<String>): PlayerCareer.PlayerCareerStats.Rank? {
        return PlayerCareer.PlayerCareerStats.Rank(
            timeFrame = getPlayerResult(data, "GROUP_VALUE").getOrNA(),
            teamId = getPlayerResult(data, "TEAM_ID")?.toIntOrNull() ?: return null,
            teamNameAbbr = getPlayerResult(data, "TEAM_ABBREVIATION").getOrNA(),
            gamePlayedRank = getPlayerResult(data, "GP_RANK")?.toIntOrNull().getOrZero(),
            winRank = getPlayerResult(data, "W_RANK")?.toIntOrNull().getOrZero(),
            loseRank = getPlayerResult(data, "L_RANK")?.toIntOrNull().getOrZero(),
            winPercentageRank = getPlayerResult(data, "W_PCT_RANK")?.toIntOrNull().getOrZero(),
            fieldGoalsMadeRank = getPlayerResult(data, "FGM_RANK")?.toIntOrNull().getOrZero(),
            fieldGoalsAttemptedRank = getPlayerResult(data, "FGA_RANK")?.toIntOrNull().getOrZero(),
            fieldGoalsPercentageRank = getPlayerResult(data, "FG_PCT_RANK")?.toIntOrNull().getOrZero(),
            threePointersMadeRank = getPlayerResult(data, "FG3M_RANK")?.toIntOrNull().getOrZero(),
            threePointersAttemptedRank = getPlayerResult(data, "FG3A_RANK")?.toIntOrNull().getOrZero(),
            threePointersPercentageRank = getPlayerResult(data, "FG3_PCT_RANK")?.toIntOrNull().getOrZero(),
            freeThrowsMadeRank = getPlayerResult(data, "FTM_RANK")?.toIntOrNull().getOrZero(),
            freeThrowsAttemptedRank = getPlayerResult(data, "FTA_RANK")?.toIntOrNull().getOrZero(),
            freeThrowsPercentageRank = getPlayerResult(data, "FT_PCT_RANK")?.toIntOrNull().getOrZero(),
            reboundsOffensiveRank = getPlayerResult(data, "OREB_RANK")?.toIntOrNull().getOrZero(),
            reboundsDefensiveRank = getPlayerResult(data, "DREB_RANK")?.toIntOrNull().getOrZero(),
            reboundsTotalRank = getPlayerResult(data, "REB_RANK")?.toIntOrNull().getOrZero(),
            assistsRank = getPlayerResult(data, "AST_RANK")?.toIntOrNull().getOrZero(),
            turnoversRank = getPlayerResult(data, "TOV_RANK")?.toIntOrNull().getOrZero(),
            stealsRank = getPlayerResult(data, "STL_RANK")?.toIntOrNull().getOrZero(),
            blocksRank = getPlayerResult(data, "BLK_RANK")?.toIntOrNull().getOrZero(),
            foulsPersonalRank = getPlayerResult(data, "PF_RANK")?.toIntOrNull().getOrZero(),
            pointsRank = getPlayerResult(data, "PTS_RANK")?.toIntOrNull().getOrZero(),
            plusMinusRank = getPlayerResult(data, "PLUS_MINUS_RANK")?.toIntOrNull().getOrZero(),
        )
    }

    private fun Double.toPercentage(): Double = this * OneHundredPercentage
}
