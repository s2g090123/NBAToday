package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.SpursColors

val teamSpurs = object : NBATeam {
    override val teamId: Int = 1610612759
    override val abbreviation: String = "SAS"
    override val teamName: String = "Spurs"
    override val location: String = "San Antonio"
    override val logoRes: Int = R.drawable.ic_team_logo_spurs
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = SpursColors
}
