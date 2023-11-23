package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.compose.theme.NetsColors

val teamNets = object : NBATeam {
    override val teamId: Int = 1610612751
    override val abbreviation: String = "BKN"
    override val teamName: String = "Nets"
    override val location: String = "Brooklyn"
    override val logoRes: Int = R.drawable.ic_team_logo_nets
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val division: NBATeam.Division = NBATeam.Division.ATLANTIC
    override val colors: NbaColors = NetsColors
}
