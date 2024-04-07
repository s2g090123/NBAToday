package com.jiachian.nbatoday.player.data.model

import android.annotation.SuppressLint
import com.jiachian.nbatoday.common.data.OneHundredPercentage
import com.jiachian.nbatoday.player.data.model.local.Player
import com.jiachian.nbatoday.player.data.model.remote.PlayerDto
import com.jiachian.nbatoday.player.data.model.remote.PlayerInfoDto
import com.jiachian.nbatoday.player.data.model.remote.PlayerStatsDto
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.getOrError
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.toPercentage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

fun PlayerDto.toPlayer(): Player? {
    val playerId = getPlayerId()
    val playerInfo = info?.toPlayerInfo()
    val playerStats = stats?.toPlayerStats()
    if (playerId == null || playerInfo == null || playerStats == null) return null
    return Player(
        playerId = playerId,
        info = playerInfo,
        stats = playerStats,
    )
}

private fun PlayerDto.getPlayerId(): Int? {
    return info?.getPlayerInfo("PERSON_ID")?.toIntOrNull()
}

private fun PlayerInfoDto.toPlayerInfo(): Player.PlayerInfo? {
    return createPlayerCareerInfo()
}

private fun PlayerInfoDto.createPlayerCareerInfo(): Player.PlayerInfo? {
    val playerName = getPlayerInfo("DISPLAY_FIRST_LAST")
    val jersey = getPlayerInfo("JERSEY")?.toIntOrNull()
    val stats = createPlayerCareerStats()
    if (playerName == null || jersey == null || stats == null) return null
    val birthDate = getFormattedBirthDate()
    val team = getPlayerInfo("TEAM_ID")?.toIntOrNull().let { teamId ->
        NBATeam.getTeamById(teamId)
    }
    return Player.PlayerInfo(
        playerName = playerName,
        playerNameAbbr = getPlayerInfo("DISPLAY_FI_LAST").getOrNA(),
        playerAge = getAge(birthDate),
        birthDate = formatBirthDate(birthDate),
        country = getPlayerInfo("COUNTRY").getOrNA(),
        school = getPlayerInfo("SCHOOL").getOrNA(),
        height = getFormattedHeight(),
        weight = getFormattedWeight(),
        seasonExperience = getPlayerInfo("SEASON_EXP")?.toIntOrNull().getOrZero(),
        jersey = jersey,
        position = getPlayerInfo("POSITION").getOrNA(),
        team = team,
        draftYear = getPlayerInfo("DRAFT_YEAR")?.toIntOrNull().getOrZero(),
        draftRound = getPlayerInfo("DRAFT_ROUND")?.toIntOrNull().getOrZero(),
        draftNumber = getPlayerInfo("DRAFT_NUMBER")?.toIntOrNull().getOrZero(),
        isGreatest75 = getPlayerInfo("GREATEST_75_FLAG") == "Y",
        headlineStats = stats
    )
}

private fun PlayerInfoDto.createPlayerCareerStats(): Player.PlayerInfo.HeadlineStats? {
    val timeFrame = getPlayerStats("TimeFrame")
    val points = getPlayerStats("PTS")?.toDoubleOrNull()
    val assists = getPlayerStats("AST")?.toDoubleOrNull()
    val rebounds = getPlayerStats("REB")?.toDoubleOrNull()
    val impact = getPlayerStats("PIE")?.toDoubleOrNull()?.times(OneHundredPercentage)?.decimalFormat()
    val timeValid = timeFrame != null
    val statsValid = points != null && assists != null && rebounds != null && impact != null
    if (!timeValid || !statsValid) return null
    return Player.PlayerInfo.HeadlineStats(
        points.getOrError(),
        assists.getOrError(),
        rebounds.getOrError(),
        impact.getOrError()
    )
}

@SuppressLint("SimpleDateFormat")
private fun formatBirthDate(birthdate: Date): String {
    val format = SimpleDateFormat("MMM dd, yyyy").apply {
        timeZone = TimeZone.getTimeZone("EST")
    }
    return format.format(birthdate)
}

private fun getAge(birthdate: Date): Int {
    val cal = DateUtils.getCalendar()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)
    cal.time = birthdate
    val birthYear = cal.get(Calendar.YEAR)
    val birthMonth = cal.get(Calendar.MONTH)
    val birthDay = cal.get(Calendar.DAY_OF_MONTH)
    var age = year - birthYear
    if (month < birthMonth || month == birthMonth && day < birthDay) {
        age--
    }
    return age
}

private fun PlayerStatsDto.toPlayerStats(): Player.PlayerStats {
    val stats = result?.rowData?.mapNotNull { data ->
        createPlayerCareerStats(data)
    } ?: emptyList()
    return Player.PlayerStats(stats)
}

private fun PlayerStatsDto.createPlayerCareerStats(data: List<String>): Player.PlayerStats.Stats? {
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
