package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.MagicColors
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamMagic = object : NBATeam {
    override val teamId: Int = 1610612753
    override val abbreviation: String = "ORL"
    override val teamName: String = "Magic"
    override val location: String = "Orlando"
    override val logoRes: Int = R.drawable.ic_team_logo_magic
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NbaColors = MagicColors
}
