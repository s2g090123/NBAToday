package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.KingsColors
import com.jiachian.nbatoday.compose.theme.NBAColors

val teamKings = object : NBATeam {
    override val teamId: Int = 1610612758
    override val abbreviation: String = "SAC"
    override val teamName: String = "Kings"
    override val location: String = "Sacramento"
    override val logoRes: Int = R.drawable.ic_team_logo_kings
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.PACIFIC
    override val colors: NBAColors = KingsColors
}
