package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.MavericksColors
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamMavericks = object : NBATeam {
    override val teamId: Int = 1610612742
    override val abbreviation: String = "DAL"
    override val teamName: String = "Mavericks"
    override val location: String = "Dallas"
    override val logoRes: Int = R.drawable.ic_team_logo_mavericks
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = MavericksColors
}
