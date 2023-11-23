package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.CelticsColors
import com.jiachian.nbatoday.compose.theme.NbaColors

val teamCeltics = object : NBATeam {
    override val teamId: Int = 1610612738
    override val abbreviation: String = "BOS"
    override val teamName: String = "Celtics"
    override val location: String = "Boston"
    override val logoRes: Int = R.drawable.ic_team_logo_celtics
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val division: NBATeam.Division = NBATeam.Division.ATLANTIC
    override val colors: NbaColors = CelticsColors
}
