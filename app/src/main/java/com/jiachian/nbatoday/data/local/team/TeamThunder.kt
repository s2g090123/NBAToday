package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.compose.theme.ThunderColors

val teamThunder = object : NBATeam {
    override val teamId: Int = 1610612760
    override val abbreviation: String = "OKC"
    override val teamName: String = "Thunder"
    override val location: String = "Oklahoma City"
    override val logoRes: Int = R.drawable.ic_team_logo_thunder
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.NORTHWEST
    override val colors: NbaColors = ThunderColors
}
