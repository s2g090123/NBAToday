package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.SunsColors

val teamSuns = object : NBATeam {
    override val teamId: Int = 1610612756
    override val abbreviation: String = "PHX"
    override val teamName: String = "Suns"
    override val location: String = "Phoenix"
    override val logoRes: Int = R.drawable.ic_team_logo_suns
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = SunsColors
}
