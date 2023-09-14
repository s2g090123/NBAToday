package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.JazzColors
import com.jiachian.nbatoday.compose.theme.NBAColors

val teamJazz = object : NBATeam {
    override val teamId: Int = 1610612762
    override val abbreviation: String = "UTA"
    override val teamName: String = "Jazz"
    override val location: String = "Utah"
    override val logoRes: Int = R.drawable.ic_team_logo_jazz
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.NORTHWEST
    override val colors: NBAColors = JazzColors
}
