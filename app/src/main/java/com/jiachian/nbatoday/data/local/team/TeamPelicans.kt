package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.PelicansColors

val teamPelicans = object : NBATeam {
    override val teamId: Int = 1610612740
    override val abbreviation: String = "NOP"
    override val teamName: String = "Pelicans"
    override val location: String = "New Orleans"
    override val logoRes: Int = R.drawable.ic_team_logo_pelicans
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.SOUTHWEST
    override val colors: NBAColors = PelicansColors
}
