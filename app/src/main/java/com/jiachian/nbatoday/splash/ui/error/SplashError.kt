package com.jiachian.nbatoday.splash.ui.error

import com.jiachian.nbatoday.home.schedule.domain.error.UpdateScheduleError
import com.jiachian.nbatoday.team.domain.error.AddTeamsError

enum class SplashError(val message: String) {
    UPDATE_SCHEDULE_FAILED("When updating the schedule, some issues occurred. Please restart the APP."),
    UPDATE_TEAM_FAILED("When updating the teams, some issues occurred. Please restart the APP."),
}

fun UpdateScheduleError.asSplashError(): SplashError {
    return when (this) {
        UpdateScheduleError.UPDATE_FAILED -> SplashError.UPDATE_SCHEDULE_FAILED
    }
}

fun AddTeamsError.asSplashError(): SplashError {
    return when (this) {
        AddTeamsError.ADD_FAILED -> SplashError.UPDATE_TEAM_FAILED
    }
}
