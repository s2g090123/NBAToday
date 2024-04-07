package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.MagicColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamMagic = object : NBATeam {
    override val teamId: Int = 1610612753
    override val abbreviation: String = "ORL"
    override val teamName: String = "Magic"
    override val location: String = "Orlando"
    override val logoRes: Int = R.drawable.ic_team_logo_magic
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = MagicColors
}
