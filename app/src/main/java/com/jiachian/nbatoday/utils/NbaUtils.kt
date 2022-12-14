package com.jiachian.nbatoday.utils

import com.jiachian.nbatoday.CDN_BASE_URL
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import java.util.*

object NbaUtils {
    fun getCalendar(): Calendar {
        val cal = Calendar.getInstance()
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

    // returns svg
    fun getPlayerImageUrlById(playerId: Int): String {
        return "${CDN_BASE_URL}headshots/nba/latest/260x190/$playerId.png"
    }

    fun formatScoreboardGameDate(year: Int, month: Int, day: Int): String {
        return String.format("%d-%d-%d", year, month, day)
    }
}