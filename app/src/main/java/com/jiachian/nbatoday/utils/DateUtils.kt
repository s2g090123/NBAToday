package com.jiachian.nbatoday.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    const val DAY_IN_MILLIS = DateUtils.DAY_IN_MILLIS

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

    /**
     * Gets a Calendar instance set to the current date and time in the "EST" time zone.
     */
    fun getCalendar(): Calendar {
        return Calendar.getInstance().apply {
            timeZone = TimeZone.getTimeZone("EST")
            time = Date()
        }
    }

    fun formatScoreboardGameDate(year: Int, month: Int, day: Int): String {
        return formatDate(year, month, day)
    }

    /**
     * Formats a date as a string in the format "yyyy-MM-dd".
     */
    fun formatDate(year: Int, month: Int, day: Int): String {
        return String.format(Locale.US, "%d-%d-%d", year, month, day)
    }

    /**
     * Parses a date represented by the given year, month, and day into a Date object.
     */
    @SuppressLint("SimpleDateFormat")
    fun parseDate(year: Int, month: Int, day: Int): Date? {
        return SimpleDateFormat("yyyy-MM-dd").let {
            it.timeZone = TimeZone.getTimeZone("EST")
            it.parse(formatDate(year, month, day))
        }
    }

    /**
     * Parses a date represented by the provided date string in "yyyy-MM-dd" format into a Date object.
     */
    @SuppressLint("SimpleDateFormat")
    fun parseDate(dateInFormat: String): Date? {
        return SimpleDateFormat("yyyy-MM-dd").let {
            it.timeZone = TimeZone.getTimeZone("EST")
            it.parse(dateInFormat)
        }
    }

    /**
     * Parses a date represented by the given date string using the provided date format.
     */
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
