package com.jiachian.nbatoday.utils

import android.annotation.SuppressLint
import com.jiachian.nbatoday.CdnBaseUrl
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

object NbaUtils {
    fun getCalendar(): Calendar {
        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("EST")
        cal.time = Date()
        return cal
    }

    // returns svg
    fun getTeamLogoUrlById(teamId: Int): String {
        return "${CdnBaseUrl}logos/nba/$teamId/global/L/logo.svg"
    }

    fun getTeamSmallLogoUrlById(teamId: Int): String {
        return "${CdnBaseUrl}logos/nba/$teamId/primary/L/logo.svg"
    }

    // returns svg
    fun getPlayerImageUrlById(playerId: Int): String {
        return "${CdnBaseUrl}headshots/nba/latest/260x190/$playerId.png"
    }

    fun formatScoreboardGameDate(year: Int, month: Int, day: Int): String {
        return formatDate(year, month, day)
    }

    fun formatDate(year: Int, month: Int, day: Int): String {
        return String.format("%d-%d-%d", year, month, day)
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDate(year: Int, month: Int, day: Int): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd").apply {
            timeZone = TimeZone.getTimeZone("EST")
        }
        return sdf.parse(formatDate(year, month, day))
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDate(dateInFormat: String): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd").apply {
            timeZone = TimeZone.getTimeZone("EST")
        }
        return sdf.parse(dateInFormat)
    }
}
