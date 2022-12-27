package com.jiachian.nbatoday.data.remote.team

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats

data class RemoteTeamStats(
    @SerializedName("resultSets") val data: List<Data>?
) {
    data class Data(
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    fun toLocal(): List<TeamStats> {
        val headers = data?.firstOrNull()?.headers ?: return emptyList()
        val rowData = data.firstOrNull()?.rowData ?: return emptyList()
        val output = mutableListOf<TeamStats>()
        rowData.forEach { data ->
            val teamId = data.getOrNull(headers.indexOf("TEAM_ID"))?.toIntOrNull() ?: return@forEach
            val team = DefaultTeam.getTeamById(teamId)
            output.add(
                TeamStats(
                    teamId,
                    team.teamName,
                    team.abbreviation,
                    team.conference,
                    team.division,
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
