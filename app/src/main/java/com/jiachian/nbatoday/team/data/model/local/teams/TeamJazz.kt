package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.JazzColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamJazz = object : NBATeam {
    override val teamId: Int = 1610612762
    override val abbreviation: String = "UTA"
    override val teamName: String = "Jazz"
    override val location: String = "Utah"
    override val logoRes: Int = R.drawable.ic_team_logo_jazz
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = JazzColors
}
