package com.jiachian.nbatoday.models.remote

import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.remote.team.RemoteTeam
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayer

object RemoteTeamFactory {
    fun getRemoteTeamPlayerStats(): RemoteTeamPlayer {
        return RemoteTeamPlayer(
            parameters = RemoteTeamPlayer.RemoteParameters(
                teamId = HomeTeamId
            ),
            data = listOf(
                RemoteTeamPlayer.RemoteResult(
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
                            HomePlayerId.toString(),
                            HomePlayerFullName,
                            HomePlayerFullName,
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
                            BasicNumber.toString()
                        )
                    )
                )
            )
        )
    }

    fun getRemoteTeamStats(): RemoteTeam {
        return RemoteTeam(
            data = listOf(
                RemoteTeam.RemoteResult(
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
                            HomeTeamId.toString(),
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
                            BasicNumber.toString()
                        )
                    )
                )
            )
        )
    }
}
