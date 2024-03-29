package com.jiachian.nbatoday.compose.screen.score.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamRowData
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.data.teamOfficial

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
