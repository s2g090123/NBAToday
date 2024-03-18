package com.jiachian.nbatoday.compose.screen.calendar.event

import java.util.Date

sealed class CalendarEvent {
    class SelectDate(val date: Date) : CalendarEvent()
    object NextMonth : CalendarEvent()
    object PrevMonth : CalendarEvent()
}
