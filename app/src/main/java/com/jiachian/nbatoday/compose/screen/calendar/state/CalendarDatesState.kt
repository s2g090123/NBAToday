package com.jiachian.nbatoday.compose.screen.calendar.state

import com.jiachian.nbatoday.compose.screen.calendar.models.CalendarDate
import java.util.Date

data class CalendarDatesState(
    val calendarDates: List<CalendarDate> = emptyList(),
    val selectedDate: Date = Date(),
    val loading: Boolean = false,
)
