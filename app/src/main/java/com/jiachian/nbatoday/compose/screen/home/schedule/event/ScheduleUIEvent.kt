package com.jiachian.nbatoday.compose.screen.home.schedule.event

import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData

sealed class ScheduleUIEvent {
    object Refresh : ScheduleUIEvent()
    class SelectDate(val date: DateData) : ScheduleUIEvent()
    object EventReceived : ScheduleUIEvent()
}
