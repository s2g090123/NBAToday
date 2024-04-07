package com.jiachian.nbatoday.calendar.ui.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.calendar.ui.model.CalendarDate
import java.util.Date

@Stable
interface CalendarDatesState {
    val calendarDates: List<CalendarDate>
    val selectedDate: Date
    val loading: Boolean
}

class MutableCalendarDatesState : CalendarDatesState {
    override var calendarDates: List<CalendarDate> by mutableStateOf(emptyList())
    override var selectedDate: Date by mutableStateOf(Date())
    override var loading: Boolean by mutableStateOf(false)
}
