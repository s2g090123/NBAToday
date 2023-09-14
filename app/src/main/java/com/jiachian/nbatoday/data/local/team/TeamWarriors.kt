package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.WarriorsColors

val teamWarriors = object : NBATeam {
    override val teamId: Int = 1610612744
    override val abbreviation: String = "GSW"
    override val teamName: String = "Warriors"
    override val location: String = "Golden State"
    override val logoRes: Int = R.drawable.ic_team_logo_warriors
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.PACIFIC
    override val colors: NBAColors = WarriorsColors
}
