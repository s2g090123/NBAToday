package com.jiachian.nbatoday.calendar.ui.event

import java.util.Date

sealed class CalendarUIEvent {
    class SelectDate(val date: Date) : CalendarUIEvent()
    object NextMonth : CalendarUIEvent()
    object PrevMonth : CalendarUIEvent()
}
