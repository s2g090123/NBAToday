package com.jiachian.nbatoday.compose.screen.calendar.event

import java.util.Date

sealed class CalendarUIEvent {
    class SelectDate(val date: Date) : CalendarUIEvent()
    object NextMonth : CalendarUIEvent()
    object PrevMonth : CalendarUIEvent()
}
