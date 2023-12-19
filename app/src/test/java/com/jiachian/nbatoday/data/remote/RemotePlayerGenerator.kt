package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.HomePlayerFirstName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.HomeTeamName
import com.jiachian.nbatoday.TeamCity
import com.jiachian.nbatoday.models.remote.player.RemotePlayer
import com.jiachian.nbatoday.models.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.models.remote.player.RemotePlayerStats

object RemotePlayerGenerator {
    fun getHome(): RemotePlayer {
        return RemotePlayer(
            info = getHomePlayerInfo(),
            stats = getHomePlayerStats()
        )
    }

    private fun getHomePlayerStats(): RemotePlayerStats {
        return RemotePlayerStats(
            parameters = RemotePlayerStats.RemoteParameter(HomePlayerId),
            resultSets = listOf(getPlayerStatsResult())
        )
    }

    private fun getPlayerStatsResult(): RemotePlayerStats.RemoteResult {
        return RemotePlayerStats.RemoteResult(
            name = "ByYearPlayerDashboard",
            headers = listOf(
                "GROUP_VALUE",
                "TEAM_ID",
                "TEAM_ABBREVIATION",
                "GP",
                "W",
                "L",
                "W_PCT",
                "FGM",
                "FGA",
                "FG_PCT",
                "FG3M",
                "FG3A",
                "FG3_PCT",
                "FTM",
                "FTA",
                "FT_PCT",
                "OREB",
                "DREB",
                "REB",
                "AST",
                "TOV",
                "STL",
                "BLK",
                "PF",
                "PTS",
                "PLUS_MINUS",
                "GP_RANK",
                "W_RANK",
                "L_RANK",
                "W_PCT_RANK",
                "FGM_RANK",
                "FGA_RANK",
                "FG_PCT_RANK",
                "FG3M_RANK",
                "FG3A_RANK",
                "FG3_PCT_RANK",
                "FTM_RANK",
                "FTA_RANK",
                "FT_PCT_RANK",
                "OREB_RANK",
                "DREB_RANK",
                "REB_RANK",
                "AST_RANK",
                "TOV_RANK",
                "STL_RANK",
                "BLK_RANK",
                "PF_RANK",
                "PTS_RANK",
                "PLUS_MINUS_RANK"
            ),
            rowData = listOf(
                listOf(
                    BasicNumber.toString(),
                    HomeTeamId.toString(),
                    HomeTeamAbbr,
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                )
            )
        )
    }

    private fun getHomePlayerInfo(): RemotePlayerInfo {
        return RemotePlayerInfo(
            resultSets = getPlayerInfoResults()
        )
    }

    private fun getPlayerInfoResults(): List<RemotePlayerInfo.RemoteResult> {
        return listOf(
            RemotePlayerInfo.RemoteResult(
                name = "CommonPlayerInfo",
                headers = listOf(
                    "PERSON_ID",
                    "DISPLAY_FIRST_LAST",
                    "DISPLAY_FI_LAST",
                    "COUNTRY",
                    "SCHOOL",
                    "HEIGHT",
                    "WEIGHT",
                    "SEASON_EXP",
                    "JERSEY",
                    "POSITION",
                    "TEAM_ID",
                    "TEAM_NAME",
                    "TEAM_ABBREVIATION",
                    "TEAM_CITY",
                    "FROM_YEAR",
                    "TO_YEAR",
                    "DRAFT_YEAR",
                    "DRAFT_ROUND",
                    "DRAFT_NUMBER",
                    "GREATEST_75_FLAG"
                ),
                rowData = listOf(
                    listOf(
                        HomePlayerId.toString(),
                        HomePlayerFirstName,
                        HomePlayerLastName,
                        TeamCity,
                        TeamCity,
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicPosition,
                        HomeTeamId.toString(),
                        HomeTeamName,
                        HomeTeamAbbr,
                        TeamCity,
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        "Y"
                    )
                )
            ),
            RemotePlayerInfo.RemoteResult(
                name = "PlayerHeadlineStats",
                headers = listOf(
                    "TimeFrame",
                    "PTS",
                    "AST",
                    "REB",
                    "PIE"
                ),
                rowData = listOf(
                    listOf(
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString()
                    )
                )
            )
        )
    }
}
