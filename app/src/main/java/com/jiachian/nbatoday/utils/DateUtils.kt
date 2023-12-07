package com.jiachian.nbatoday.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    private const val MONTH_JAN = 0
    private const val MONTH_FEB = 1
    private const val MONTH_MAR = 2
    private const val MONTH_APR = 3
    private const val MONTH_MAY = 4
    private const val MONTH_JUN = 5
    private const val MONTH_JUL = 6
    private const val MONTH_AUG = 7
    private const val MONTH_SEP = 8
    private const val MONTH_OCT = 9
    private const val MONTH_NOV = 10

    fun getCalendar(): Calendar {
        return Calendar.getInstance().apply {
            timeZone = TimeZone.getTimeZone("EST")
            time = Date()
        }
    }

    fun formatScoreboardGameDate(year: Int, month: Int, day: Int): String {
        return formatDate(year, month, day)
    }

    fun formatDate(year: Int, month: Int, day: Int): String {
        return String.format(Locale.US, "%d-%d-%d", year, month, day)
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDate(year: Int, month: Int, day: Int): Date? {
        return SimpleDateFormat("yyyy-MM-dd").let {
            it.timeZone = TimeZone.getTimeZone("EST")
            it.parse(formatDate(year, month, day))
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDate(dateInFormat: String): Date? {
        return SimpleDateFormat("yyyy-MM-dd").let {
            it.timeZone = TimeZone.getTimeZone("EST")
            it.parse(dateInFormat)
        }
    }

    fun parseDate(dateString: String?, dateFormat: SimpleDateFormat): Date? {
        return dateString?.let { time ->
            try {
                dateFormat.parse(time)
            } catch (e: ParseException) {
                null
            }
        }
    }

    fun getDateString(year: Int, month: Int): String {
        return when (month) {
            MONTH_JAN -> "Jan"
            MONTH_FEB -> "Feb"
            MONTH_MAR -> "Mar"
            MONTH_APR -> "Apr"
            MONTH_MAY -> "May"
            MONTH_JUN -> "Jun"
            MONTH_JUL -> "Jul"
            MONTH_AUG -> "Aug"
            MONTH_SEP -> "Sep"
            MONTH_OCT -> "Oct"
            MONTH_NOV -> "Nov"
            else -> "Dec"
        } + "  " + year
    }
}
