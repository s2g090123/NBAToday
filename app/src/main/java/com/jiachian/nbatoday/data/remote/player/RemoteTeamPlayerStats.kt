package com.jiachian.nbatoday.data.remote.player

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero

data class RemoteTeamPlayerStats(
    @SerializedName("parameters") val parameters: Parameters?,
    @SerializedName("resultSets") val data: List<Result>?
) {
    data class Parameters(
        @SerializedName("TeamID") val teamId: Int?
    )

    data class Result(
        @SerializedName("name") val name: String?,
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    private var result: Result? = null

    fun toLocal(): List<PlayerStats> {
        val teamId = parameters?.teamId ?: return emptyList()
        result = data?.find { it.name == "PlayersSeasonTotals" }
        val rowData = result?.rowData ?: return emptyList()
        val output = mutableListOf<PlayerStats>()
        rowData.forEach { data ->
            val stats = createPlayerStats(data, teamId) ?: return@forEach
            output.add(stats)
        }
        return output
    }

    private fun getPlayerResult(data: List<String>, name: String): String? {
        val headers = result?.headers ?: return null
        return data.getOrNull(headers.indexOf(name))
    }

    private fun createPlayerStats(data: List<String>, teamId: Int): PlayerStats? {
        return PlayerStats(
            playerId = getPlayerResult(data, "PLAYER_ID")?.toIntOrNull() ?: return null,
            teamId = teamId,
            playerName = getPlayerResult(data, "PLAYER_NAME").getOrNA(),
            playerNickName = getPlayerResult(data, "NICKNAME").getOrNA(),
            gamePlayed = getPlayerResult(data, "GP")?.toIntOrNull().getOrZero(),
            win = getPlayerResult(data, "W")?.toIntOrNull().getOrZero(),
            lose = getPlayerResult(data, "L")?.toIntOrNull().getOrZero(),
            winPercentage = getPlayerResult(data, "W_PCT")?.toDoubleOrNull()?.times(100).getOrZero(),
            fieldGoalsMade = getPlayerResult(data, "FGM")?.toIntOrNull().getOrZero(),
            fieldGoalsAttempted = getPlayerResult(data, "FGA")?.toIntOrNull().getOrZero(),
            fieldGoalsPercentage = getPlayerResult(data, "FG_PCT")?.toDoubleOrNull()?.times(100).getOrZero(),
            threePointersMade = getPlayerResult(data, "FG3M")?.toIntOrNull().getOrZero(),
            threePointersAttempted = getPlayerResult(data, "FG3A")?.toIntOrNull().getOrZero(),
            threePointersPercentage = getPlayerResult(data, "FG3_PCT")?.toDoubleOrNull()?.times(100).getOrZero(),
            freeThrowsMade = getPlayerResult(data, "FTM")?.toIntOrNull().getOrZero(),
            freeThrowsAttempted = getPlayerResult(data, "FTA")?.toIntOrNull().getOrZero(),
            freeThrowsPercentage = getPlayerResult(data, "FT_PCT")?.toDoubleOrNull()?.times(100).getOrZero(),
            reboundsOffensive = getPlayerResult(data, "OREB")?.toIntOrNull().getOrZero(),
            reboundsDefensive = getPlayerResult(data, "DREB")?.toIntOrNull().getOrZero(),
            reboundsTotal = getPlayerResult(data, "REB")?.toIntOrNull().getOrZero(),
            assists = getPlayerResult(data, "AST")?.toIntOrNull().getOrZero(),
            turnovers = getPlayerResult(data, "TOV")?.toDoubleOrNull()?.toInt().getOrZero(),
            steals = getPlayerResult(data, "STL")?.toIntOrNull().getOrZero(),
            blocks = getPlayerResult(data, "BLK")?.toIntOrNull().getOrZero(),
            foulsPersonal = getPlayerResult(data, "PF")?.toIntOrNull().getOrZero(),
            points = getPlayerResult(data, "PTS")?.toIntOrNull().getOrZero(),
            plusMinus = getPlayerResult(data, "PLUS_MINUS")?.toDoubleOrNull()?.toInt().getOrZero()
        )
    }
}
