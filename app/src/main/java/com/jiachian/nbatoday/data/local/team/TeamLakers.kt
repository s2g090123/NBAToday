package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.compose.theme.NbaColors

val teamLakers = object : NBATeam {
    override val teamId: Int = 1610612747
    override val abbreviation: String = "LAL"
    override val teamName: String = "Lakers"
    override val location: String = "Los Angeles"
    override val logoRes: Int = R.drawable.ic_team_logo_lakers
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.PACIFIC
    override val colors: NbaColors = LakersColors
}
