package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.PacersColors

val teamPacers = object : NBATeam {
    override val teamId: Int = 1610612754
    override val abbreviation: String = "IND"
    override val teamName: String = "Pacers"
    override val location: String = "Indiana"
    override val logoRes: Int = R.drawable.ic_team_logo_pacers
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val division: NBATeam.Division = NBATeam.Division.CENTRAL
    override val colors: NBAColors = PacersColors
}
