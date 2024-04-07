package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.ClippersColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamClippers = object : NBATeam {
    override val teamId: Int = 1610612746
    override val abbreviation: String = "LAC"
    override val teamName: String = "Clippers"
    override val location: String = "Los Angeles"
    override val logoRes: Int = R.drawable.ic_team_logo_clippers
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = ClippersColors
}
