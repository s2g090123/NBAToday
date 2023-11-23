package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.compose.theme.NuggetsColors

val teamNuggets = object : NBATeam {
    override val teamId: Int = 1610612743
    override val abbreviation: String = "DEN"
    override val teamName: String = "Nuggets"
    override val location: String = "Denver"
    override val logoRes: Int = R.drawable.ic_team_logo_nuggets
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.NORTHWEST
    override val colors: NbaColors = NuggetsColors
}
