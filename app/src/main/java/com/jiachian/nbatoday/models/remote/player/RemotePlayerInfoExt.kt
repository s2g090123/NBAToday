package com.jiachian.nbatoday.models.remote.player

import android.annotation.SuppressLint
import com.jiachian.nbatoday.models.local.player.PlayerCareer
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.getOrError
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

fun RemotePlayerInfo.toPlayerInfo(): PlayerCareer.PlayerCareerInfo? {
    return createPlayerCareerInfo()
}

private fun RemotePlayerInfo.createPlayerCareerInfo(): PlayerCareer.PlayerCareerInfo? {
    val playerName = getPlayerInfo("DISPLAY_FIRST_LAST")
    val jersey = getPlayerInfo("JERSEY")?.toIntOrNull()
    val stats = createPlayerCareerStats()
    if (playerName == null || jersey == null || stats == null) return null
    val birthDate = getFormattedBirthDate()
    val team = getPlayerInfo("TEAM_ID")?.toIntOrNull().let { teamId ->
        NBATeam.getTeamById(teamId)
    }
    return PlayerCareer.PlayerCareerInfo(
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

private fun RemotePlayerInfo.createPlayerCareerStats(): PlayerCareer.PlayerCareerInfo.HeadlineStats? {
    val timeFrame = getPlayerStats("TimeFrame")
    val points = getPlayerStats("PTS")?.toDoubleOrNull()
    val assists = getPlayerStats("AST")?.toDoubleOrNull()
    val rebounds = getPlayerStats("REB")?.toDoubleOrNull()
    val impact = getPlayerStats("PIE")?.toDoubleOrNull()?.times(100)?.decimalFormat()
    val isTimeValid = timeFrame != null
    val isStatsValid = points != null && assists != null && rebounds != null && impact != null
    if (!isTimeValid || !isStatsValid) return null
    return PlayerCareer.PlayerCareerInfo.HeadlineStats(
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
    val cal = NbaUtils.getCalendar()
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
