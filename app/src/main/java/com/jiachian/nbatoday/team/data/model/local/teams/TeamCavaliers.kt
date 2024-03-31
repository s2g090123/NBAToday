package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.CavaliersColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamCavaliers = object : NBATeam {
    override val teamId: Int = 1610612739
    override val abbreviation: String = "CLE"
    override val teamName: String = "Cavaliers"
    override val location: String = "Cleveland"
    override val logoRes: Int = R.drawable.ic_team_logo_cavaliers
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = CavaliersColors
}
