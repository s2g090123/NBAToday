package com.jiachian.nbatoday.data.remote.player

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.local.player.PlayerStats

data class RemoteTeamPlayerStats(
    @SerializedName("parameters") val parameters: Parameters?,
    @SerializedName("resultSets") val data: List<Data>?
) {
    data class Parameters(
        @SerializedName("TeamID") val teamId: Int?
    )

    data class Data(
        @SerializedName("name") val name: String?,
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    fun toLocal(): List<PlayerStats> {
        val teamId = parameters?.teamId ?: return emptyList()
        val target = data?.firstOrNull { it.name == "PlayersSeasonTotals" } ?: return emptyList()
        val headers = target.headers ?: return emptyList()
        val rowData = target.rowData ?: return emptyList()
        val output = mutableListOf<PlayerStats>()
        rowData.forEach { data ->
            output.add(
                PlayerStats(
                    data.getOrNull(headers.indexOf("PLAYER_ID"))?.toIntOrNull() ?: return@forEach,
                    teamId,
                    data.getOrNull(headers.indexOf("PLAYER_NAME")) ?: return@forEach,
                    data.getOrNull(headers.indexOf("NICKNAME")) ?: return@forEach,
                    data.getOrNull(headers.indexOf("GP"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("W"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("L"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("W_PCT"))?.toDoubleOrNull()?.times(100)
                        ?: return@forEach,
                    data.getOrNull(headers.indexOf("FGM"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FGA"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FG_PCT"))?.toDoubleOrNull()?.times(100)
                        ?: return@forEach,
                    data.getOrNull(headers.indexOf("FG3M"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FG3A"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FG3_PCT"))?.toDoubleOrNull()?.times(100)
                        ?: return@forEach,
                    data.getOrNull(headers.indexOf("FTM"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FTA"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FT_PCT"))?.toDoubleOrNull()?.times(100)
                        ?: return@forEach,
                    data.getOrNull(headers.indexOf("OREB"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("DREB"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("REB"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("AST"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("TOV"))?.toDoubleOrNull()?.toInt()
                        ?: return@forEach,
                    data.getOrNull(headers.indexOf("STL"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("BLK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("PF"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("PTS"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("PLUS_MINUS"))?.toDoubleOrNull()?.toInt()
                        ?: return@forEach
                )
            )
        }
        return output
    }
}
