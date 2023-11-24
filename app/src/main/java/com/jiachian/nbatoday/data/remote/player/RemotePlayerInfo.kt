package com.jiachian.nbatoday.data.remote.player

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.getOrError
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

private const val InchPerFoot = 12
private const val CentiMetersPerInch = 2.54
private const val KilogramPerPound = 0.45

data class RemotePlayerInfo(
    @SerializedName("resultSets") val resultSets: List<Result>?
) {
    data class Result(
        @SerializedName("name") val name: String?,
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    @Volatile
    private var infoResultImp: Result? = null

    @Volatile
    private var statsResultImp: Result? = null

    private val infoResult: Result?
        @Synchronized
        get() {
            return infoResultImp ?: resultSets?.find { it.name == "CommonPlayerInfo" }?.also {
                infoResultImp = it
            }
        }

    private val statsResult: Result?
        @Synchronized
        get() {
            return statsResultImp ?: resultSets?.find { it.name == "PlayerHeadlineStats" }?.also {
                statsResultImp = it
            }
        }

    fun toUpdateData(): PlayerCareerInfoUpdate? {
        val personId = getPlayerInfo("PERSON_ID")?.toIntOrNull()
        val info = createPlayerCareerInfo()
        if (personId == null || info == null) return null
        return PlayerCareerInfoUpdate(personId, info)
    }

    private fun getPlayerInfo(name: String): String? {
        val playerInfoRowData = infoResult?.rowData?.getOrNull(0)
        val playerInfoHeaders = infoResult?.headers
        if (playerInfoRowData == null || playerInfoHeaders == null) return null
        return playerInfoRowData.getOrNull(playerInfoHeaders.indexOf(name))
    }

    private fun getPlayerStats(name: String): String? {
        val headlineRowData = statsResult?.rowData?.getOrNull(0)
        val headlineHeaders = statsResult?.headers
        if (headlineRowData == null || headlineHeaders == null) return null
        return headlineRowData.getOrNull(headlineHeaders.indexOf(name))
    }

    private fun createPlayerCareerInfo(): PlayerCareer.PlayerCareerInfo? {
        val playerName = getPlayerInfo("DISPLAY_FIRST_LAST")
        val jersey = getPlayerInfo("JERSEY")?.toIntOrNull()
        val stats = createPlayerCareerStats()
        if (playerName == null || jersey == null || stats == null) return null
        val birthDate = getBirthDate()
        val team = getPlayerInfo("TEAM_ID")?.toIntOrNull()?.let { teamId ->
            NBATeam.getTeamById(teamId)
        } ?: teamOfficial
        return PlayerCareer.PlayerCareerInfo(
            playerName = playerName,
            playerNameAbbr = getPlayerInfo("DISPLAY_FI_LAST").getOrNA(),
            playerAge = getAge(birthDate),
            birthDate = formatBirthDate(birthDate),
            country = getPlayerInfo("COUNTRY").getOrNA(),
            school = getPlayerInfo("SCHOOL").getOrNA(),
            height = getHeight(),
            weight = getWeight(),
            seasonExperience = getPlayerInfo("SEASON_EXP")?.toIntOrNull().getOrZero(),
            jersey = jersey,
            position = getPlayerInfo("POSITION").getOrNA(),
            team = team,
            fromYear = getPlayerInfo("FROM_YEAR")?.toIntOrNull().getOrZero(),
            toYear = getPlayerInfo("TO_YEAR")?.toIntOrNull().getOrZero(),
            draftYear = getPlayerInfo("DRAFT_YEAR")?.toIntOrNull().getOrZero(),
            draftRound = getPlayerInfo("DRAFT_ROUND")?.toIntOrNull().getOrZero(),
            draftNumber = getPlayerInfo("DRAFT_NUMBER")?.toIntOrNull().getOrZero(),
            isGreatest75 = getPlayerInfo("GREATEST_75_FLAG") == "Y",
            headlineStats = stats
        )
    }

    private fun createPlayerCareerStats(): PlayerCareer.PlayerCareerInfo.HeadlineStats? {
        val timeFrame = getPlayerStats("TimeFrame")
        val points = getPlayerStats("PTS")?.toDoubleOrNull()
        val assists = getPlayerStats("AST")?.toDoubleOrNull()
        val rebounds = getPlayerStats("REB")?.toDoubleOrNull()
        val impact = getPlayerStats("PIE")?.toDoubleOrNull()
        val isTimeValid = timeFrame != null
        val isStatsValid = points != null && assists != null && rebounds != null && impact != null
        if (!isTimeValid || !isStatsValid) return null
        return PlayerCareer.PlayerCareerInfo.HeadlineStats(
            timeFrame.getOrError(),
            points.getOrError(),
            assists.getOrError(),
            rebounds.getOrError(),
            impact.getOrError()
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun getBirthDate(): Date {
        val cal = NbaUtils.getCalendar()
        val currentDate = cal.time
        return getPlayerInfo("BIRTHDATE")?.let {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
                timeZone = TimeZone.getTimeZone("EST")
            }
            sdf.parse(it)
        } ?: currentDate
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

    private fun getHeight(): Double {
        return getPlayerInfo("HEIGHT")?.split("-")?.let {
            val foot = it.getOrNull(0)?.toIntOrNull() ?: 0
            val inches = it.getOrNull(1)?.toIntOrNull() ?: 0
            (foot * InchPerFoot + inches) * CentiMetersPerInch
        } ?: return 0.0
    }

    private fun getWeight(): Double {
        return getPlayerInfo("WEIGHT")?.let { lb ->
            (lb.toIntOrNull() ?: 0) * KilogramPerPound
        } ?: return 0.0
    }
}
