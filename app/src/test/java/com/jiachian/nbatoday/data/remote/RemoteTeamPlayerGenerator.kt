package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayer

object RemoteTeamPlayerGenerator {
    fun getHome(): RemoteTeamPlayer {
        return RemoteTeamPlayer(
            parameters = getHomeParameters(),
            data = listOf(getResult())
        )
    }

    private fun getHomeParameters(): RemoteTeamPlayer.RemoteParameters {
        return RemoteTeamPlayer.RemoteParameters(
            teamId = HomeTeamId
        )
    }

    private fun getResult(): RemoteTeamPlayer.RemoteResult {
        return RemoteTeamPlayer.RemoteResult(
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
    }
}
