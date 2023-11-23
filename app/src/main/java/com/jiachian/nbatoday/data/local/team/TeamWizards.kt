package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.compose.theme.WizardsColors

val teamWizards = object : NBATeam {
    override val teamId: Int = 1610612764
    override val abbreviation: String = "WAS"
    override val teamName: String = "Wizards"
    override val location: String = "Washington"
    override val logoRes: Int = R.drawable.ic_team_logo_wizards
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val division: NBATeam.Division = NBATeam.Division.SOUTHEAST
    override val colors: NbaColors = WizardsColors
}
