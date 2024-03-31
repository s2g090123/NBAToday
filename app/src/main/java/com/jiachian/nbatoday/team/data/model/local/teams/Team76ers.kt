package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.P76ersColors

val team76ers = object : NBATeam {
    override val teamId: Int = 1610612755
    override val abbreviation: String = "PHI"
    override val teamName: String = "76ers"
    override val location: String = "Philadelphia"
    override val logoRes: Int = R.drawable.ic_team_logo_76ers
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = P76ersColors
}
