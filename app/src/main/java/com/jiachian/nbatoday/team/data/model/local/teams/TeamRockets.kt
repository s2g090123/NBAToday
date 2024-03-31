package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.RocketsColors

val teamRockets = object : NBATeam {
    override val teamId: Int = 1610612745
    override val abbreviation: String = "HOU"
    override val teamName: String = "Rockets"
    override val location: String = "Houston"
    override val logoRes: Int = R.drawable.ic_team_logo_rockets
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = RocketsColors
}
