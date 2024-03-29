package com.jiachian.nbatoday.compose.screen.team.event

import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerSorting

sealed class TeamUIEvent {
    class Sort(val sorting: TeamPlayerSorting) : TeamUIEvent()
    object EventReceived : TeamUIEvent()
}
