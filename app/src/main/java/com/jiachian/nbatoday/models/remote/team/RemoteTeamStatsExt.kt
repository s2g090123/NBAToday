package com.jiachian.nbatoday.models.remote.team

import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamStats
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.toPercentage

fun RemoteTeamStats.toTeamStats(): List<TeamStats> {
    val rowData = result?.rowData ?: return emptyList()
    return rowData.mapNotNull { data ->
        createTeamStats(data) ?: return@mapNotNull null
    }
}

private fun RemoteTeamStats.createTeamStats(data: List<String>): TeamStats? {
    val teamId = getStatsResult(data, "TEAM_ID")?.toIntOrNull() ?: return null
    val team = NBATeam.getTeamById(teamId)
    return TeamStats(
        teamId = teamId,
        team = team,
        teamConference = team.conference,
        gamePlayed = getStatsResult(data, "GP")?.toIntOrNull().getOrZero(),
        win = getStatsResult(data, "W")?.toIntOrNull().getOrZero(),
        lose = getStatsResult(data, "L")?.toIntOrNull().getOrZero(),
        winPercentage = getStatsResult(data, "W_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        fieldGoalsMade = getStatsResult(data, "FGM")?.toIntOrNull().getOrZero(),
        fieldGoalsAttempted = getStatsResult(data, "FGA")?.toIntOrNull().getOrZero(),
        fieldGoalsPercentage = getStatsResult(data, "FG_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        threePointersMade = getStatsResult(data, "FG3M")?.toIntOrNull().getOrZero(),
        threePointersAttempted = getStatsResult(data, "FG3A")?.toIntOrNull().getOrZero(),
        threePointersPercentage = getStatsResult(data, "FG3_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        freeThrowsMade = getStatsResult(data, "FTM")?.toIntOrNull().getOrZero(),
        freeThrowsAttempted = getStatsResult(data, "FTA")?.toIntOrNull().getOrZero(),
        freeThrowsPercentage = getStatsResult(data, "FT_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        reboundsOffensive = getStatsResult(data, "OREB")?.toIntOrNull().getOrZero(),
        reboundsDefensive = getStatsResult(data, "DREB")?.toIntOrNull().getOrZero(),
        reboundsTotal = getStatsResult(data, "REB")?.toIntOrNull().getOrZero(),
        assists = getStatsResult(data, "AST")?.toIntOrNull().getOrZero(),
        turnovers = getStatsResult(data, "TOV")?.toDoubleOrNull()?.toInt().getOrZero(),
        steals = getStatsResult(data, "STL")?.toIntOrNull().getOrZero(),
        blocks = getStatsResult(data, "BLK")?.toIntOrNull().getOrZero(),
        foulsPersonal = getStatsResult(data, "PF")?.toIntOrNull().getOrZero(),
        points = getStatsResult(data, "PTS")?.toIntOrNull().getOrZero(),
        plusMinus = getStatsResult(data, "PLUS_MINUS")?.toDoubleOrNull().getOrZero().decimalFormat()
    )
}
