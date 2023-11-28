package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.compose.theme.RaptorsColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamRaptors = object : NBATeam {
    override val teamId: Int = 1610612761
    override val abbreviation: String = "TOR"
    override val teamName: String = "Raptors"
    override val location: String = "Toronto"
    override val logoRes: Int = R.drawable.ic_team_logo_raptors
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NbaColors = RaptorsColors
}
