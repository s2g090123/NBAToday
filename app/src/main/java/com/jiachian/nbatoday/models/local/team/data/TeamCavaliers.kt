package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.CavaliersColors
import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamCavaliers = object : NBATeam {
    override val teamId: Int = 1610612739
    override val abbreviation: String = "CLE"
    override val teamName: String = "Cavaliers"
    override val location: String = "Cleveland"
    override val logoRes: Int = R.drawable.ic_team_logo_cavaliers
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NbaColors = CavaliersColors
}
