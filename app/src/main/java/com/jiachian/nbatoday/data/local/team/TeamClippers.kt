package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.ClippersColors
import com.jiachian.nbatoday.compose.theme.NbaColors

val teamClippers = object : NBATeam {
    override val teamId: Int = 1610612746
    override val abbreviation: String = "LAC"
    override val teamName: String = "Clippers"
    override val location: String = "Los Angeles"
    override val logoRes: Int = R.drawable.ic_team_logo_clippers
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.PACIFIC
    override val colors: NbaColors = ClippersColors
}
