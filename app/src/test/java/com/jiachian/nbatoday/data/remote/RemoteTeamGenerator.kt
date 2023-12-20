package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.remote.team.RemoteTeam

object RemoteTeamGenerator {
    fun get(): RemoteTeam {
        return RemoteTeam(
            data = listOf(getResult(HomeTeamId), getResult(AwayTeamId))
        )
    }

    fun get(teamId: Int): RemoteTeam {
        return RemoteTeam(
            data = listOf(getResult(teamId))
        )
    }

    private fun getResult(teamId: Int): RemoteTeam.RemoteResult {
        return RemoteTeam.RemoteResult(
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
                    teamId.toString(),
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
    }
}
