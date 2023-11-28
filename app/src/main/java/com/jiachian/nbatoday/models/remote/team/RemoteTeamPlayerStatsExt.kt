package com.jiachian.nbatoday.models.remote.team

import com.jiachian.nbatoday.models.local.team.TeamPlayerStats
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.toPercentage

fun RemoteTeamPlayerStats.toTeamPlayerStats(): List<TeamPlayerStats> {
    val teamId = parameters?.teamId ?: return emptyList()
    val rowData = result?.rowData
    return rowData?.mapNotNull { data ->
        createPlayerStats(data, teamId) ?: return@mapNotNull null
    } ?: emptyList()
}

private fun RemoteTeamPlayerStats.createPlayerStats(data: List<String>, teamId: Int): TeamPlayerStats? {
    return TeamPlayerStats(
        playerId = getPlayerResult(data, "PLAYER_ID")?.toIntOrNull() ?: return null,
        teamId = teamId,
        playerName = getPlayerResult(data, "PLAYER_NAME").getOrNA(),
        gamePlayed = getPlayerResult(data, "GP")?.toIntOrNull().getOrZero(),
        win = getPlayerResult(data, "W")?.toIntOrNull().getOrZero(),
        lose = getPlayerResult(data, "L")?.toIntOrNull().getOrZero(),
        winPercentage = getPlayerResult(data, "W_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        fieldGoalsMade = getPlayerResult(data, "FGM")?.toIntOrNull().getOrZero(),
        fieldGoalsAttempted = getPlayerResult(data, "FGA")?.toIntOrNull().getOrZero(),
        fieldGoalsPercentage = getPlayerResult(data, "FG_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        threePointersMade = getPlayerResult(data, "FG3M")?.toIntOrNull().getOrZero(),
        threePointersAttempted = getPlayerResult(data, "FG3A")?.toIntOrNull().getOrZero(),
        threePointersPercentage = getPlayerResult(data, "FG3_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        freeThrowsMade = getPlayerResult(data, "FTM")?.toIntOrNull().getOrZero(),
        freeThrowsAttempted = getPlayerResult(data, "FTA")?.toIntOrNull().getOrZero(),
        freeThrowsPercentage = getPlayerResult(data, "FT_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        reboundsOffensive = getPlayerResult(data, "OREB")?.toIntOrNull().getOrZero(),
        reboundsDefensive = getPlayerResult(data, "DREB")?.toIntOrNull().getOrZero(),
        reboundsTotal = getPlayerResult(data, "REB")?.toIntOrNull().getOrZero(),
        assists = getPlayerResult(data, "AST")?.toIntOrNull().getOrZero(),
        turnovers = getPlayerResult(data, "TOV")?.toDoubleOrNull()?.toInt().getOrZero(),
        steals = getPlayerResult(data, "STL")?.toIntOrNull().getOrZero(),
        blocks = getPlayerResult(data, "BLK")?.toIntOrNull().getOrZero(),
        foulsPersonal = getPlayerResult(data, "PF")?.toIntOrNull().getOrZero(),
        points = getPlayerResult(data, "PTS")?.toIntOrNull().getOrZero(),
        plusMinus = getPlayerResult(data, "PLUS_MINUS")?.toDoubleOrNull()?.toInt().getOrZero()
    )
}
