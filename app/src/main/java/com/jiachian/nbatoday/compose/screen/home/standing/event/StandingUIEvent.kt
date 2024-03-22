package com.jiachian.nbatoday.compose.screen.home.standing.event

import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingSorting
import com.jiachian.nbatoday.models.local.team.NBATeam

sealed class StandingUIEvent {
    object Refresh : StandingUIEvent()
    class SelectConference(val conference: NBATeam.Conference) : StandingUIEvent()
    class UpdateSorting(val sorting: StandingSorting) : StandingUIEvent()
    object EventReceived : StandingUIEvent()
}
