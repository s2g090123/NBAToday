package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.BlazersColors
import com.jiachian.nbatoday.compose.theme.NbaColors

val teamBlazers = object : NBATeam {
    override val teamId: Int = 1610612757
    override val abbreviation: String = "POR"
    override val teamName: String = "Blazers"
    override val location: String = "Portland"
    override val logoRes: Int = R.drawable.ic_team_logo_blazers
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.NORTHWEST
    override val colors: NbaColors = BlazersColors
}
