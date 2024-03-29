package com.jiachian.nbatoday.compose.screen.team.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerRowData
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerSorting

@Stable
interface TeamPlayersState {
    val players: List<TeamPlayerRowData>
    val sorting: TeamPlayerSorting
}

class MutableTeamPlayersState : TeamPlayersState {
    override var players: List<TeamPlayerRowData> by mutableStateOf(emptyList())
    override var sorting: TeamPlayerSorting by mutableStateOf(TeamPlayerSorting.PTS)
}
