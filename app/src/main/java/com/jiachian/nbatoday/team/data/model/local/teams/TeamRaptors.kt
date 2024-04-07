package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.RaptorsColors

val teamRaptors = object : NBATeam {
    override val teamId: Int = 1610612761
    override val abbreviation: String = "TOR"
    override val teamName: String = "Raptors"
    override val location: String = "Toronto"
    override val logoRes: Int = R.drawable.ic_team_logo_raptors
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = RaptorsColors
}
