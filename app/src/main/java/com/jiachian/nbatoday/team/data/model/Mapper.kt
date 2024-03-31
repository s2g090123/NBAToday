package com.jiachian.nbatoday.team.data.model

import com.jiachian.nbatoday.team.data.model.local.Team
import com.jiachian.nbatoday.team.data.model.local.TeamPlayer
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.team.data.model.remote.TeamDto
import com.jiachian.nbatoday.team.data.model.remote.TeamPlayerDto
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.toPercentage

fun TeamDto.toTeamStats(): List<Team> {
    return result?.rowData?.mapNotNull { data ->
        createTeamStats(data)
    } ?: emptyList()
}

private fun TeamDto.createTeamStats(data: List<String>): Team? {
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

fun TeamPlayerDto.toTeamPlayerStats(): List<TeamPlayer> {
    val teamId = parameters?.teamId ?: return emptyList()
    return result?.rowData?.mapNotNull { data ->
        createPlayerStats(data, teamId)
    } ?: emptyList()
}

private fun TeamPlayerDto.createPlayerStats(data: List<String>, teamId: Int): TeamPlayer? {
    return TeamPlayer(
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
