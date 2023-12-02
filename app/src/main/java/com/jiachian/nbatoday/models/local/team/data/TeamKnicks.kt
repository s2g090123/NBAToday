package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.KnicksColors
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamKnicks = object : NBATeam {
    override val teamId: Int = 1610612752
    override val abbreviation: String = "NYK"
    override val teamName: String = "Knicks"
    override val location: String = "New York"
    override val logoRes: Int = R.drawable.ic_team_logo_knicks
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = KnicksColors
}
