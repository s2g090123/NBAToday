package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.MavericksColors
import com.jiachian.nbatoday.compose.theme.NbaColors

val teamMavericks = object : NBATeam {
    override val teamId: Int = 1610612742
    override val abbreviation: String = "DAL"
    override val teamName: String = "Mavericks"
    override val location: String = "Dallas"
    override val logoRes: Int = R.drawable.ic_team_logo_mavericks
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.SOUTHWEST
    override val colors: NbaColors = MavericksColors
}
