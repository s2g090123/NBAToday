package com.jiachian.nbatoday.data.remote.player

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate

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

    fun toLocal(): PlayerCareerStatsUpdate? {
        val playerId = parameters?.playerId ?: return null
        val results = resultSets ?: return null
        val result = results.getOrNull(results.indexOfFirst { it.name == "ByYearPlayerDashboard" })
            ?: return null
        val headers = result.headers ?: return null
        val stats = arrayListOf<PlayerCareer.PlayerCareerStats.Stats>()
        val ranks = arrayListOf<PlayerCareer.PlayerCareerStats.Rank>()
        result.rowData?.forEach { data ->
            stats.add(
                PlayerCareer.PlayerCareerStats.Stats(
                    data.getOrNull(headers.indexOf("GROUP_VALUE")) ?: return@forEach,
                    data.getOrNull(headers.indexOf("TEAM_ID"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("TEAM_ABBREVIATION")) ?: return@forEach,
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
                    data.getOrNull(headers.indexOf("TOV"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("STL"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("BLK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("PF"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("PTS"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("PLUS_MINUS"))?.toIntOrNull() ?: return@forEach,
                )
            )
            ranks.add(
                PlayerCareer.PlayerCareerStats.Rank(
                    data.getOrNull(headers.indexOf("GROUP_VALUE")) ?: return@forEach,
                    data.getOrNull(headers.indexOf("TEAM_ID"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("TEAM_ABBREVIATION")) ?: return@forEach,
                    data.getOrNull(headers.indexOf("GP_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("W_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("L_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("W_PCT_RANK"))?.toIntOrNull()
                        ?: return@forEach,
                    data.getOrNull(headers.indexOf("FGM_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FGA_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FG_PCT_RANK"))?.toIntOrNull()
                        ?: return@forEach,
                    data.getOrNull(headers.indexOf("FG3M_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FG3A_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FG3_PCT_RANK"))?.toIntOrNull()
                        ?: return@forEach,
                    data.getOrNull(headers.indexOf("FTM_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FTA_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("FT_PCT_RANK"))?.toIntOrNull()
                        ?: return@forEach,
                    data.getOrNull(headers.indexOf("OREB_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("DREB_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("REB_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("AST_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("TOV_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("STL_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("BLK_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("PF_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("PTS_RANK"))?.toIntOrNull() ?: return@forEach,
                    data.getOrNull(headers.indexOf("PLUS_MINUS_RANK"))?.toIntOrNull()
                        ?: return@forEach,
                )
            )
        }
        return PlayerCareerStatsUpdate(
            playerId,
            PlayerCareer.PlayerCareerStats(
                stats,
                ranks
            )
        )
    }
}
