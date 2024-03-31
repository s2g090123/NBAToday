package com.jiachian.nbatoday.home.standing.ui.event

import com.jiachian.nbatoday.home.standing.ui.model.StandingSorting
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam

sealed class StandingUIEvent {
    object Refresh : StandingUIEvent()
    class SelectConference(val conference: NBATeam.Conference) : StandingUIEvent()
    class UpdateSorting(val sorting: StandingSorting) : StandingUIEvent()
    object EventReceived : StandingUIEvent()
}
