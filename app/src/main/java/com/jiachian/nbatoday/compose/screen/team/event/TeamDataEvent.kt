package com.jiachian.nbatoday.compose.screen.team.event

import com.jiachian.nbatoday.compose.screen.team.TeamError

sealed class TeamDataEvent {
    class Error(val error: TeamError) : TeamDataEvent()
}
