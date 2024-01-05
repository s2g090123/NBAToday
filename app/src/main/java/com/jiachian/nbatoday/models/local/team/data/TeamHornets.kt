package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.HornetsColors
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamHornets = object : NBATeam {
    override val teamId: Int = 1610612766
    override val abbreviation: String = "CHA"
    override val teamName: String = "Hornets"
    override val location: String = "Charlotte"
    override val logoRes: Int = R.drawable.ic_team_logo_hornets
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = HornetsColors
}
