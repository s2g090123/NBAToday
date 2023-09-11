package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.BASIC_NUMBER
import com.jiachian.nbatoday.HOME_PLAYER_FULL_NAME
import com.jiachian.nbatoday.HOME_PLAYER_ID
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats

object RemoteTeamFactory {
    fun getRemoteTeamPlayerStats(): RemoteTeamPlayerStats {
        return RemoteTeamPlayerStats(
            parameters = RemoteTeamPlayerStats.Parameters(
                teamId = HOME_TEAM_ID
            ),
            data = listOf(
                RemoteTeamPlayerStats.Data(
                    name = "PlayersSeasonTotals",
                    headers = listOf(
                        "PLAYER_ID",
                        "PLAYER_NAME",
                        "NICKNAME",
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
                        "PLUS_MINUS"
                    ),
                    rowData = listOf(
                        listOf(
                            HOME_PLAYER_ID.toString(),
                            HOME_PLAYER_FULL_NAME,
                            HOME_PLAYER_FULL_NAME,
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
                            BASIC_NUMBER.toString()
                        )
                    )
                )
            )
        )
    }

    fun getRemoteTeamStats(): RemoteTeamStats {
        return RemoteTeamStats(
            data = listOf(
                RemoteTeamStats.Data(
                    name = "LeagueDashTeamStats",
                    headers = listOf(
                        "TEAM_ID",
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
                        "PLUS_MINUS"
                    ),
                    rowData = listOf(
                        listOf(
                            HOME_TEAM_ID.toString(),
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
                            BASIC_NUMBER.toString()
                        )
                    )
                )
            )
        )
    }
}
