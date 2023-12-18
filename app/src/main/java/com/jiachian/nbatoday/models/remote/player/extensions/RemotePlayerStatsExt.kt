package com.jiachian.nbatoday.models.remote.player.extensions

import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.toPercentage

fun RemotePlayerStats.toPlayerStats(): Player.PlayerStats {
    val stats = result?.rowData?.mapNotNull { data ->
        createPlayerCareerStats(data)
    } ?: emptyList()
    return Player.PlayerStats(stats)
}

private fun RemotePlayerStats.createPlayerCareerStats(data: List<String>): Player.PlayerStats.Stats? {
    return Player.PlayerStats.Stats(
        timeFrame = getPlayerResult(data, "GROUP_VALUE").getOrNA(),
        teamId = getPlayerResult(data, "TEAM_ID")?.toIntOrNull() ?: return null,
        teamNameAbbr = getPlayerResult(data, "TEAM_ABBREVIATION").getOrNA(),
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
        turnovers = getPlayerResult(data, "TOV")?.toIntOrNull().getOrZero(),
        steals = getPlayerResult(data, "STL")?.toIntOrNull().getOrZero(),
        blocks = getPlayerResult(data, "BLK")?.toIntOrNull().getOrZero(),
        foulsPersonal = getPlayerResult(data, "PF")?.toIntOrNull().getOrZero(),
        points = getPlayerResult(data, "PTS")?.toIntOrNull().getOrZero(),
        plusMinus = getPlayerResult(data, "PLUS_MINUS")?.toIntOrNull().getOrZero(),
    )
}
