package com.jiachian.nbatoday.home.standing.ui.event

import com.jiachian.nbatoday.home.standing.ui.error.StandingError

sealed class StandingDataEvent {
    class Error(val error: StandingError) : StandingDataEvent()
    class ScrollTo(val page: Int) : StandingDataEvent()
}
