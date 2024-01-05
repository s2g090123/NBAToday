package com.jiachian.nbatoday.compose.screen.team.models

import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamRank

data class TeamUI(
    val team: Team,
    val rank: TeamRank,
    val players: List<TeamPlayerRowData>,
)
