package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.compose.theme.TimberwolvesColors

val teamTimberwolves = object : NBATeam {
    override val teamId: Int = 1610612750
    override val abbreviation: String = "MIN"
    override val teamName: String = "Timberwolves"
    override val location: String = "Minnesota"
    override val logoRes: Int = R.drawable.ic_team_logo_timberwolves
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.NORTHWEST
    override val colors: NbaColors = TimberwolvesColors
}
