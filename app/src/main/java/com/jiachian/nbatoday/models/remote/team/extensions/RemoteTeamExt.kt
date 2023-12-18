package com.jiachian.nbatoday.models.remote.team.extensions

import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.remote.team.RemoteTeam
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.toPercentage

fun RemoteTeam.toTeamStats(): List<Team> {
    return result?.rowData?.mapNotNull { data ->
        createTeamStats(data)
    } ?: emptyList()
}

private fun RemoteTeam.createTeamStats(data: List<String>): Team? {
    val teamId = getStatsResult(data, "TEAM_ID")?.toIntOrNull() ?: return null
    val team = NBATeam.getTeamById(teamId)
    return Team(
        teamId = teamId,
        team = team,
        teamConference = team.conference,
        gamePlayed = getStatsResult(data, "GP")?.toIntOrNull().getOrZero(),
        win = getStatsResult(data, "W")?.toIntOrNull().getOrZero(),
        lose = getStatsResult(data, "L")?.toIntOrNull().getOrZero(),
        winPercentage = getStatsResult(data, "W_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        fieldGoalsPercentage = getStatsResult(data, "FG_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        threePointersPercentage = getStatsResult(data, "FG3_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        freeThrowsPercentage = getStatsResult(data, "FT_PCT")?.toDoubleOrNull()?.toPercentage().getOrZero(),
        reboundsOffensive = getStatsResult(data, "OREB")?.toIntOrNull().getOrZero(),
        reboundsDefensive = getStatsResult(data, "DREB")?.toIntOrNull().getOrZero(),
        reboundsTotal = getStatsResult(data, "REB")?.toIntOrNull().getOrZero(),
        assists = getStatsResult(data, "AST")?.toIntOrNull().getOrZero(),
        turnovers = getStatsResult(data, "TOV")?.toDoubleOrNull()?.toInt().getOrZero(),
        steals = getStatsResult(data, "STL")?.toIntOrNull().getOrZero(),
        blocks = getStatsResult(data, "BLK")?.toIntOrNull().getOrZero(),
        points = getStatsResult(data, "PTS")?.toIntOrNull().getOrZero(),
        plusMinus = getStatsResult(data, "PLUS_MINUS")?.toDoubleOrNull().getOrZero().decimalFormat()
    )
}
