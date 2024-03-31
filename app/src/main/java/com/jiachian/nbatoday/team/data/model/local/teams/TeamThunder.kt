package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.ThunderColors

val teamThunder = object : NBATeam {
    override val teamId: Int = 1610612760
    override val abbreviation: String = "OKC"
    override val teamName: String = "Thunder"
    override val location: String = "Oklahoma City"
    override val logoRes: Int = R.drawable.ic_team_logo_thunder
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = ThunderColors
}
