package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.LakersColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamLakers = object : NBATeam {
    override val teamId: Int = 1610612747
    override val abbreviation: String = "LAL"
    override val teamName: String = "Lakers"
    override val location: String = "Los Angeles"
    override val logoRes: Int = R.drawable.ic_team_logo_lakers
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = LakersColors
}
