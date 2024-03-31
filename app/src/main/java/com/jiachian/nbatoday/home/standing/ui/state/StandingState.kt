package com.jiachian.nbatoday.home.standing.ui.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.home.standing.ui.event.StandingDataEvent

@Stable
interface StandingState {
    val eastTeamState: StandingTeamState
    val westTeamState: StandingTeamState
    val refreshing: Boolean
    val event: StandingDataEvent?
}

class MutableStandingState : StandingState {
    override val eastTeamState: MutableStandingTeamState = MutableStandingTeamState()
    override val westTeamState: MutableStandingTeamState = MutableStandingTeamState()
    override var refreshing: Boolean by mutableStateOf(false)
    override var event: StandingDataEvent? by mutableStateOf(null)
}
