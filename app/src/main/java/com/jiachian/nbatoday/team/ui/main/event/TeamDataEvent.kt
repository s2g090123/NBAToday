package com.jiachian.nbatoday.team.ui.main.event

import com.jiachian.nbatoday.team.ui.main.error.TeamError

sealed class TeamDataEvent {
    class Error(val error: TeamError) : TeamDataEvent()
}
