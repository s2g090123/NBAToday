package com.jiachian.nbatoday.data.remote.player

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.utils.NbaUtils
import java.text.SimpleDateFormat
import java.util.*

data class RemotePlayerInfo(
    @SerializedName("resultSets") val resultSets: List<Result>?
) {
    data class Result(
        @SerializedName("name") val name: String?,
        @SerializedName("headers") val headers: List<String>?,
        @SerializedName("rowSet") val rowData: List<List<String>>?
    )

    fun toUpdateData(): PlayerCareerInfoUpdate? {
        val results = resultSets ?: return null
        val playerInfo =
            results.getOrNull(results.indexOfFirst { it.name == "CommonPlayerInfo" }) ?: return null
        val headline = results.getOrNull(results.indexOfFirst { it.name == "PlayerHeadlineStats" })
            ?: return null
        val playerInfoHeaders = playerInfo.headers ?: return null
        val playerInfoRowData = playerInfo.rowData?.getOrNull(0) ?: return null
        val headlineHeaders = headline.headers ?: return null
        val headlineRowData = headline.rowData?.getOrNull(0) ?: return null
        val cal = NbaUtils.getCalendar()
        val currentDate = cal.time
        val birthDate = playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("BIRTHDATE"))?.let {
            parseBirthdate(it)
        } ?: currentDate
        return PlayerCareerInfoUpdate(
            playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("PERSON_ID"))?.toIntOrNull()
                ?: return null,
            PlayerCareer.PlayerCareerInfo(
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("DISPLAY_FIRST_LAST"))
                    ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("DISPLAY_FI_LAST"))
                    ?: return null,
                getAge(birthDate),
                formatBirthDate(birthDate),
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("COUNTRY")) ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("SCHOOL")) ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("HEIGHT"))?.split("-")?.let {
                    heightToCM(
                        it.getOrNull(0)?.toIntOrNull() ?: 0,
                        it.getOrNull(1)?.toIntOrNull() ?: 0
                    )
                } ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("WEIGHT"))?.let {
                    weightToKG(it.toIntOrNull() ?: 0)
                } ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("SEASON_EXP"))?.toIntOrNull()
                    ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("JERSEY"))?.toIntOrNull()
                    ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("POSITION")) ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("TEAM_ID"))?.toIntOrNull()
                    ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("TEAM_NAME")) ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("TEAM_ABBREVIATION"))
                    ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("TEAM_CITY")) ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("FROM_YEAR"))?.toIntOrNull()
                    ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("TO_YEAR"))?.toIntOrNull()
                    ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("DRAFT_YEAR"))?.toIntOrNull()
                    ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("DRAFT_ROUND"))?.toIntOrNull()
                    ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("DRAFT_NUMBER"))
                    ?.toIntOrNull() ?: return null,
                playerInfoRowData.getOrNull(playerInfoHeaders.indexOf("GREATEST_75_FLAG"))?.let {
                    it == "Y"
                } ?: return null,
                PlayerCareer.PlayerCareerInfo.HeadlineStats(
                    headlineRowData.getOrNull(headlineHeaders.indexOf("TimeFrame")) ?: return null,
                    headlineRowData.getOrNull(headlineHeaders.indexOf("PTS"))?.toDoubleOrNull()
                        ?: return null,
                    headlineRowData.getOrNull(headlineHeaders.indexOf("AST"))?.toDoubleOrNull()
                        ?: return null,
                    headlineRowData.getOrNull(headlineHeaders.indexOf("REB"))?.toDoubleOrNull()
                        ?: return null,
                    headlineRowData.getOrNull(headlineHeaders.indexOf("PIE"))?.toDoubleOrNull()
                        ?: return null
                )
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun parseBirthdate(birthdate: String): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
            timeZone = TimeZone.getTimeZone("EST")
        }
        return sdf.parse(birthdate)
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

    private fun heightToCM(foot: Int, inches: Int): Double {
        return (foot * 12 + inches) * 2.54
    }

    private fun weightToKG(lb: Int): Double {
        return lb * 0.45
    }
}
