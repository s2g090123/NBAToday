package com.jiachian.nbatoday.home.schedule.ui.error

import com.jiachian.nbatoday.home.schedule.domain.error.UpdateScheduleError

sealed class ScheduleError(val message: String) {
    object RefreshFailed : ScheduleError("Refresh schedule failed.")
}

fun UpdateScheduleError.asScheduleError(): ScheduleError {
    return when (this) {
        UpdateScheduleError.UPDATE_FAILED -> ScheduleError.RefreshFailed
    }
}
