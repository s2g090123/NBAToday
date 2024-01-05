package com.jiachian.nbatoday.test.utils

import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import org.junit.Test

class DateUtilsTest {
    @Test
    fun `getCalendar() expects correct`() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("EST"))
        DateUtils
            .getCalendar()
            .assertIsTrue { it.get(Calendar.YEAR) == cal.get(Calendar.YEAR) }
            .assertIsTrue { it.get(Calendar.MONTH) == cal.get(Calendar.MONTH) }
            .assertIsTrue { it.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH) }
    }

    @Test
    fun `formatScoreboardGameDate() expects correct`() {
        DateUtils
            .formatScoreboardGameDate(2023, 1, 1)
            .assertIs("2023-1-1")
    }

    @Test
    fun `formatDate() expects correct`() {
        DateUtils
            .formatDate(2023, 1, 1)
            .assertIs("2023-1-1")
    }

    @Test
    fun `parseDate(date) expects correct`() {
        val cal = Calendar.Builder().apply {
            setTimeZone(TimeZone.getTimeZone("EST"))
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.build()
        DateUtils
            .parseDate(2023, 1, 1)
            .assertIs(cal.time)
    }

    @Test
    fun `parseDate(format) expects correct`() {
        val cal = Calendar.Builder().apply {
            setTimeZone(TimeZone.getTimeZone("EST"))
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.build()
        DateUtils
            .parseDate("2023-1-1")
            .assertIs(cal.time)
    }

    @Test
    fun `parseDate(dateString, dateFormat) expects correct`() {
        val cal = Calendar.Builder().apply {
            setTimeZone(TimeZone.getTimeZone("EST"))
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.build()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd").apply {
            timeZone = TimeZone.getTimeZone("EST")
        }
        DateUtils
            .parseDate("2023-1-1", dateFormat)
            .assertIs(cal.time)
    }

    @Test
    fun `parseDate(dateString, dateFormat) with null dateString expects null`() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd").apply {
            timeZone = TimeZone.getTimeZone("EST")
        }
        DateUtils
            .parseDate(null, dateFormat)
            .assertIsNull()
    }

    @Test
    fun `parseDate(dateString, dateFormat) with error dateString expects null`() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd").apply {
            timeZone = TimeZone.getTimeZone("EST")
        }
        DateUtils
            .parseDate("2023", dateFormat)
            .assertIsNull()
    }

    @Test
    fun `getDateString() expects correct`() {
        repeat(13) { month ->
            DateUtils
                .getDateString(2023, month)
                .assertIs(
                    when (month) {
                        0 -> "Jan"
                        1 -> "Feb"
                        2 -> "Mar"
                        3 -> "Apr"
                        4 -> "May"
                        5 -> "Jun"
                        6 -> "Jul"
                        7 -> "Aug"
                        8 -> "Sep"
                        9 -> "Oct"
                        10 -> "Nov"
                        else -> "Dec"
                    } + "  " + "2023"
                )
        }
    }
}
