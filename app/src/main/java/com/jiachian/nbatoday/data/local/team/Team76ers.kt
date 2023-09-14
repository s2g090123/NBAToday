package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.P76ersColors

val team76ers = object : NBATeam {
    override val teamId: Int = 1610612755
    override val abbreviation: String = "PHI"
    override val teamName: String = "76ers"
    override val location: String = "Philadelphia"
    override val logoRes: Int = R.drawable.ic_team_logo_76ers
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val division: NBATeam.Division = NBATeam.Division.ATLANTIC
    override val colors: NBAColors = P76ersColors
}
