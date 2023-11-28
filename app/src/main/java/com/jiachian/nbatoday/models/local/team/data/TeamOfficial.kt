package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.compose.theme.OfficialColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamOfficial = object : NBATeam {
    override val teamId: Int = 0
    override val abbreviation: String = "NBA"
    override val teamName: String = "NBA"
    override val location: String = "Official"
    override val logoRes: Int = R.drawable.ic_logo_nba
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NbaColors = OfficialColors
}
