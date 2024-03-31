package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.HeatColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamHeat = object : NBATeam {
    override val teamId: Int = 1610612748
    override val abbreviation: String = "MIA"
    override val teamName: String = "Heat"
    override val location: String = "Miami"
    override val logoRes: Int = R.drawable.ic_team_logo_heat
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = HeatColors
}
