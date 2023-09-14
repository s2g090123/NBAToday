package com.jiachian.nbatoday.data.remote.team

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.local.team.teamOfficial

data class RemoteTeamStats(
    @SerializedName("resultSets") val data: List<Data>?
) {
    data class Data(
        @SerializedName("name") val name: String?,
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    fun toLocal(): List<TeamStats> {
        val target = data?.firstOrNull { it.name == "LeagueDashTeamStats" } ?: return emptyList()
        val headers = target.headers ?: return emptyList()
        val rowData = target.rowData ?: return emptyList()
        val output = mutableListOf<TeamStats>()
        rowData.forEach { data ->
            val teamId = data.getOrNull(headers.indexOf("TEAM_ID"))?.toIntOrNull() ?: return@forEach
            val team = NBATeam.getTeamById(teamId) ?: teamOfficial
            output.add(
                TeamStats(
                    teamId,
                    team,
                    team.conference,
                    data.getOrNull(headers.indexOf("GP"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("W"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("L"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("W_PCT"))?.toDoubleOrNull()?.times(100) ?: return@forEach,
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
