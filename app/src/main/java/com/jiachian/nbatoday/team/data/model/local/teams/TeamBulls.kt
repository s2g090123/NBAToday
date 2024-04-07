package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.BullsColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamBulls = object : NBATeam {
    override val teamId: Int = 1610612741
    override val abbreviation: String = "CHI"
    override val teamName: String = "Bulls"
    override val location: String = "Chicago"
    override val logoRes: Int = R.drawable.ic_team_logo_bulls
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = BullsColors
}
