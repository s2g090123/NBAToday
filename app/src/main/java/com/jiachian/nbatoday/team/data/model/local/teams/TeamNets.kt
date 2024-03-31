package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.NetsColors

val teamNets = object : NBATeam {
    override val teamId: Int = 1610612751
    override val abbreviation: String = "BKN"
    override val teamName: String = "Nets"
    override val location: String = "Brooklyn"
    override val logoRes: Int = R.drawable.ic_team_logo_nets
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = NetsColors
}
