package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.HornetsColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamHornets = object : NBATeam {
    override val teamId: Int = 1610612766
    override val abbreviation: String = "CHA"
    override val teamName: String = "Hornets"
    override val location: String = "Charlotte"
    override val logoRes: Int = R.drawable.ic_team_logo_hornets
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = HornetsColors
}
