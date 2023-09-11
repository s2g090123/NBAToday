package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.BASIC_NUMBER
import com.jiachian.nbatoday.BASIC_POSITION
import com.jiachian.nbatoday.HOME_PLAYER_FIRST_NAME
import com.jiachian.nbatoday.HOME_PLAYER_ID
import com.jiachian.nbatoday.HOME_PLAYER_LAST_NAME
import com.jiachian.nbatoday.HOME_TEAM_ABBR
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.HOME_TEAM_NAME
import com.jiachian.nbatoday.TEAM_CITY
import com.jiachian.nbatoday.data.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats

object RemotePlayerFactory {
    fun getRemotePlayerDetail(): RemotePlayerDetail {
        return RemotePlayerDetail(
            info = getRemotePlayerInfo(),
            stats = getRemotePlayerStats()
        )
    }

    fun getRemotePlayerStats(): RemotePlayerStats {
        return RemotePlayerStats(
            parameters = RemotePlayerStats.Parameter(HOME_PLAYER_ID),
            resultSets = listOf(
                RemotePlayerStats.Result(
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
                            BASIC_NUMBER.toString(),
                            HOME_TEAM_ID.toString(),
                            HOME_TEAM_ABBR,
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                        )
                    )
                )
            )
        )
    }

    fun getRemotePlayerInfo(): RemotePlayerInfo {
        return RemotePlayerInfo(
            resultSets = listOf(
                RemotePlayerInfo.Result(
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
                            HOME_PLAYER_ID.toString(),
                            HOME_PLAYER_FIRST_NAME,
                            HOME_PLAYER_LAST_NAME,
                            TEAM_CITY,
                            TEAM_CITY,
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_POSITION,
                            HOME_TEAM_ID.toString(),
                            HOME_TEAM_NAME,
                            HOME_TEAM_ABBR,
                            TEAM_CITY,
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            "Y"
                        )
                    )
                ),
                RemotePlayerInfo.Result(
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
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString(),
                            BASIC_NUMBER.toString()
                        )
                    )
                )
            )
        )
    }
}
