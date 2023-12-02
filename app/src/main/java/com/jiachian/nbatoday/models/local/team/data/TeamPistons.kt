package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.PistonsColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamPistons = object : NBATeam {
    override val teamId: Int = 1610612765
    override val abbreviation: String = "DET"
    override val teamName: String = "Pistons"
    override val location: String = "Detroit"
    override val logoRes: Int = R.drawable.ic_team_logo_pistons
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = PistonsColors
}
