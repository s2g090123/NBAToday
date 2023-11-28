package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.compose.theme.RocketsColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamRockets = object : NBATeam {
    override val teamId: Int = 1610612745
    override val abbreviation: String = "HOU"
    override val teamName: String = "Rockets"
    override val location: String = "Houston"
    override val logoRes: Int = R.drawable.ic_team_logo_rockets
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NbaColors = RocketsColors
}
