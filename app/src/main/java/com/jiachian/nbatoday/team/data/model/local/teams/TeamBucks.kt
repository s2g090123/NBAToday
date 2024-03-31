package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.BucksColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamBucks = object : NBATeam {
    override val teamId: Int = 1610612749
    override val abbreviation: String = "MIL"
    override val teamName: String = "Bucks"
    override val location: String = "Milwaukee"
    override val logoRes: Int = R.drawable.ic_team_logo_bucks
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = BucksColors
}
