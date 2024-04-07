package com.jiachian.nbatoday.home.schedule.ui.event

import com.jiachian.nbatoday.home.schedule.ui.model.DateData

sealed class ScheduleUIEvent {
    object Refresh : ScheduleUIEvent()
    class SelectDate(val date: DateData) : ScheduleUIEvent()
    object EventReceived : ScheduleUIEvent()
}
