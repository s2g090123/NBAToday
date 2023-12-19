package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.remote.team.RemoteTeam

object RemoteTeamGenerator {
    fun getHome(): RemoteTeam {
        return RemoteTeam(
            data = listOf(getResult())
        )
    }

    private fun getResult(): RemoteTeam.RemoteResult {
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
    }
}
