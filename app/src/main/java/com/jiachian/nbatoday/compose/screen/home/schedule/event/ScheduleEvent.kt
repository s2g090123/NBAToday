package com.jiachian.nbatoday.compose.screen.home.schedule.event

import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData

sealed class ScheduleEvent {
    object Refresh : ScheduleEvent()
    class Select(val date: DateData) : ScheduleEvent()
}
