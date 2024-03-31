package com.jiachian.nbatoday.team.ui.main.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.team.data.model.local.Team
import com.jiachian.nbatoday.team.data.model.local.TeamRank

@Stable
interface TeamInfoState {
    val team: Team
    val rank: TeamRank
}

class MutableTeamInfoState : TeamInfoState {
    override var team: Team by mutableStateOf(Team())
    override var rank: TeamRank by mutableStateOf(TeamRank())
}
