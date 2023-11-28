package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.compose.theme.P76ersColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val team76ers = object : NBATeam {
    override val teamId: Int = 1610612755
    override val abbreviation: String = "PHI"
    override val teamName: String = "76ers"
    override val location: String = "Philadelphia"
    override val logoRes: Int = R.drawable.ic_team_logo_76ers
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NbaColors = P76ersColors
}
