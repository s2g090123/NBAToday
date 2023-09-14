package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.HeatColors
import com.jiachian.nbatoday.compose.theme.NBAColors

val teamHeat = object : NBATeam {
    override val teamId: Int = 1610612748
    override val abbreviation: String = "MIA"
    override val teamName: String = "Heat"
    override val location: String = "Miami"
    override val logoRes: Int = R.drawable.ic_team_logo_heat
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val division: NBATeam.Division = NBATeam.Division.SOUTHEAST
    override val colors: NBAColors = HeatColors
}
