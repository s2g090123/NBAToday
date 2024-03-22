package com.jiachian.nbatoday.compose.screen.home.standing

import com.jiachian.nbatoday.usecase.team.AddTeamsError

sealed class StandingError(val message: String) {
    object RefreshFailed : StandingError("Refreshing is failed.")
}

fun AddTeamsError.asStandingError(): StandingError {
    return when (this) {
        AddTeamsError.ADD_FAILED -> StandingError.RefreshFailed
    }
}
