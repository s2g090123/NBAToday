package com.jiachian.nbatoday.home.schedule.ui.event

import com.jiachian.nbatoday.home.schedule.ui.error.ScheduleError

sealed class ScheduleDataEvent {
    class Error(val error: ScheduleError) : ScheduleDataEvent()
}
