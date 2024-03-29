package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.GrizzliesColors
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamGrizzlies = object : NBATeam {
    override val teamId: Int = 1610612763
    override val abbreviation: String = "MEM"
    override val teamName: String = "Grizzlies"
    override val location: String = "Memphis"
    override val logoRes: Int = R.drawable.ic_team_logo_grizzlies
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = GrizzliesColors
}
