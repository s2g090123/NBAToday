package com.jiachian.nbatoday.compose.screen.home.schedule

import com.jiachian.nbatoday.usecase.schedule.UpdateScheduleError

sealed class ScheduleError(val message: String) {
    object RefreshFailed : ScheduleError("Refresh schedule failed.")
}

fun UpdateScheduleError.asScheduleError(): ScheduleError {
    return when (this) {
        UpdateScheduleError.UPDATE_FAILED -> ScheduleError.RefreshFailed
    }
}
