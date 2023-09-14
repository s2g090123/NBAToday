package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.BullsColors
import com.jiachian.nbatoday.compose.theme.NBAColors

val teamBulls = object : NBATeam {
    override val teamId: Int = 1610612741
    override val abbreviation: String = "CHI"
    override val teamName: String = "Bulls"
    override val location: String = "Chicago"
    override val logoRes: Int = R.drawable.ic_team_logo_bulls
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val division: NBATeam.Division = NBATeam.Division.CENTRAL
    override val colors: NBAColors = BullsColors
}
