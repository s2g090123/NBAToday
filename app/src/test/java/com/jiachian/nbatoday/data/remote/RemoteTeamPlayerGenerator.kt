package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.team.data.model.remote.TeamPlayerDto

object RemoteTeamPlayerGenerator {
    fun get(teamId: Int): TeamPlayerDto {
        return TeamPlayerDto(
            parameters = getParameters(teamId),
            data = listOf(getResult(teamId))
        )
    }

    private fun getParameters(teamId: Int): TeamPlayerDto.RemoteParameters {
        return TeamPlayerDto.RemoteParameters(
            teamId = teamId
        )
    }

    private fun getResult(teamId: Int): TeamPlayerDto.RemoteResult {
        val playerId = if (teamId == HomeTeamId) HomePlayerId else AwayPlayerId
        val playerName = if (teamId == HomeTeamId) HomePlayerFullName else AwayPlayerLastName
        return TeamPlayerDto.RemoteResult(
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
                    playerId.toString(),
                    playerName,
                    playerName,
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
                ),
                listOf(
                    (playerId + 1).toString(),
                    playerName,
                    playerName,
                    (BasicNumber - 1).toString(),
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
