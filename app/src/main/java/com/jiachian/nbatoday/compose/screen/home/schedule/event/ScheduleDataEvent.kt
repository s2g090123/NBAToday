package com.jiachian.nbatoday.compose.screen.home.schedule.event

import com.jiachian.nbatoday.compose.screen.home.schedule.ScheduleError

sealed class ScheduleDataEvent {
    class Error(val error: ScheduleError) : ScheduleDataEvent()
}
