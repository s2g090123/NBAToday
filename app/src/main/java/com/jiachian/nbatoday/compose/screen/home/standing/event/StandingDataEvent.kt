package com.jiachian.nbatoday.compose.screen.home.standing.event

import com.jiachian.nbatoday.compose.screen.home.standing.StandingError

sealed class StandingDataEvent {
    class Error(val error: StandingError) : StandingDataEvent()
    class ScrollTo(val page: Int) : StandingDataEvent()
}
