package com.jiachian.nbatoday.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
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
}
