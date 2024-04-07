package com.jiachian.nbatoday.team.ui.main.event

import com.jiachian.nbatoday.team.ui.player.model.TeamPlayerSorting

sealed class TeamUIEvent {
    class Sort(val sorting: TeamPlayerSorting) : TeamUIEvent()
    object EventReceived : TeamUIEvent()
}
