package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.OfficialColors

val teamOfficial = object : NBATeam {
    override val teamId: Int = 0
    override val abbreviation: String = "NBA"
    override val teamName: String = "NBA"
    override val location: String = "Official"
    override val logoRes: Int = R.drawable.ic_logo_nba
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = OfficialColors
}
