package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.WarriorsColors

val teamWarriors = object : NBATeam {
    override val teamId: Int = 1610612744
    override val abbreviation: String = "GSW"
    override val teamName: String = "Warriors"
    override val location: String = "Golden State"
    override val logoRes: Int = R.drawable.ic_team_logo_warriors
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = WarriorsColors
}
