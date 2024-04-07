package com.jiachian.nbatoday.boxscore.ui.team.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.boxscore.ui.team.model.BoxScoreTeamRowData
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.team.data.model.local.teams.teamOfficial

@Stable
interface BoxScoreTeamState {
    val homeTeam: NBATeam
    val awayTeam: NBATeam
    val data: List<BoxScoreTeamRowData>
}

class MutableBoxScoreTeamState : BoxScoreTeamState {
    override var homeTeam: NBATeam by mutableStateOf(teamOfficial)
    override var awayTeam: NBATeam by mutableStateOf(teamOfficial)
    override var data: List<BoxScoreTeamRowData> by mutableStateOf(emptyList())
}
