package com.jiachian.nbatoday.home.standing.ui.error

import com.jiachian.nbatoday.team.domain.error.AddTeamsError

sealed class StandingError(val message: String) {
    object RefreshFailed : StandingError("Refreshing is failed.")
}

fun AddTeamsError.asStandingError(): StandingError {
    return when (this) {
        AddTeamsError.ADD_FAILED -> StandingError.RefreshFailed
    }
}
