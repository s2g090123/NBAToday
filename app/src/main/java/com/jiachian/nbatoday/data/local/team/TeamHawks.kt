package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.HawksColors
import com.jiachian.nbatoday.compose.theme.NbaColors

val teamHawks = object : NBATeam {
    override val teamId: Int = 1610612737
    override val abbreviation: String = "ATL"
    override val teamName: String = "Hawks"
    override val location: String = "Atlanta"
    override val logoRes: Int = R.drawable.ic_team_logo_hawks
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val division: NBATeam.Division = NBATeam.Division.SOUTHEAST
    override val colors: NbaColors = HawksColors
}
