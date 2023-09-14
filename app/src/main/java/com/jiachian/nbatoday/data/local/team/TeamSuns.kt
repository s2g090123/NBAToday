package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.SunsColors

val teamSuns = object : NBATeam {
    override val teamId: Int = 1610612756
    override val abbreviation: String = "PHX"
    override val teamName: String = "Suns"
    override val location: String = "Phoenix"
    override val logoRes: Int = R.drawable.ic_team_logo_suns
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.PACIFIC
    override val colors: NBAColors = SunsColors
}
