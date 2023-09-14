package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.RocketsColors

val teamRockets = object : NBATeam {
    override val teamId: Int = 1610612745
    override val abbreviation: String = "HOU"
    override val teamName: String = "Rockets"
    override val location: String = "Houston"
    override val logoRes: Int = R.drawable.ic_team_logo_rockets
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.SOUTHWEST
    override val colors: NBAColors = RocketsColors
}
