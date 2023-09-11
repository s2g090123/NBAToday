package com.jiachian.nbatoday.utils

import android.annotation.SuppressLint
import com.jiachian.nbatoday.CDN_BASE_URL
import com.jiachian.nbatoday.data.local.team.DefaultTeam
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

    fun getTeamLogoResById(teamId: Int): Int {
        return DefaultTeam.getTeamById(teamId).logoRes
    }

    // returns svg
    fun getTeamLogoUrlById(teamId: Int): String {
        return "${CDN_BASE_URL}logos/nba/$teamId/global/L/logo.svg"
    }

    fun getTeamSmallLogoUrlById(teamId: Int): String {
        return "${CDN_BASE_URL}logos/nba/$teamId/primary/L/logo.svg"
    }

    // returns svg
    fun getPlayerImageUrlById(playerId: Int): String {
        return "${CDN_BASE_URL}headshots/nba/latest/260x190/$playerId.png"
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
