package com.jiachian.nbatoday.compose.screen.score.models

import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.team.NBATeam

data class BoxScoreUI(
    val boxScore: BoxScore,
    val periods: List<String>,
    val players: BoxScorePlayersUI,
    val teams: BoxScoreTeamsUI,
    val leaders: BoxScoreLeadersUI,
) {
    data class BoxScorePlayersUI(
        val home: List<BoxScorePlayerRowData>,
        val away: List<BoxScorePlayerRowData>,
    )

    data class BoxScoreTeamsUI(
        val home: NBATeam,
        val away: NBATeam,
        val rowData: List<BoxScoreTeamRowData>,
    )

    data class BoxScoreLeadersUI(
        val home: BoxScore.BoxScoreTeam.Player?,
        val away: BoxScore.BoxScoreTeam.Player?,
        val rowData: List<BoxScoreLeaderRowData>,
    )
}
