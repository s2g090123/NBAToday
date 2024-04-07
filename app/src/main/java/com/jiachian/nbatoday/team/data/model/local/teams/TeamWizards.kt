package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.WizardsColors

val teamWizards = object : NBATeam {
    override val teamId: Int = 1610612764
    override val abbreviation: String = "WAS"
    override val teamName: String = "Wizards"
    override val location: String = "Washington"
    override val logoRes: Int = R.drawable.ic_team_logo_wizards
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = WizardsColors
}
