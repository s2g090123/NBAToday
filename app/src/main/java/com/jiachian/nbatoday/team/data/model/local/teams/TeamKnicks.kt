package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.KnicksColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamKnicks = object : NBATeam {
    override val teamId: Int = 1610612752
    override val abbreviation: String = "NYK"
    override val teamName: String = "Knicks"
    override val location: String = "New York"
    override val logoRes: Int = R.drawable.ic_team_logo_knicks
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = KnicksColors
}
