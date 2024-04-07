package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.HawksColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamHawks = object : NBATeam {
    override val teamId: Int = 1610612737
    override val abbreviation: String = "ATL"
    override val teamName: String = "Hawks"
    override val location: String = "Atlanta"
    override val logoRes: Int = R.drawable.ic_team_logo_hawks
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = HawksColors
}
