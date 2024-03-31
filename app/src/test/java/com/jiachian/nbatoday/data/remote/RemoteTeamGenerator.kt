package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.team.data.model.remote.TeamDto

object RemoteTeamGenerator {
    fun get(): TeamDto {
        return TeamDto(
            data = listOf(getResult(listOf(HomeTeamId, AwayTeamId)))
        )
    }

    fun get(teamId: Int): TeamDto {
        return TeamDto(
            data = listOf(getResult(listOf(teamId)))
        )
    }

    private fun getResult(teamId: List<Int>): TeamDto.RemoteResult {
        return TeamDto.RemoteResult(
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
            rowData = teamId.map { getRowData(it) }
        )
    }

    private fun getRowData(teamId: Int): List<String> {
        return listOf(
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
    }
}
