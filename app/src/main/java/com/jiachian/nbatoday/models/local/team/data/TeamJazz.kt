package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.JazzColors
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamJazz = object : NBATeam {
    override val teamId: Int = 1610612762
    override val abbreviation: String = "UTA"
    override val teamName: String = "Jazz"
    override val location: String = "Utah"
    override val logoRes: Int = R.drawable.ic_team_logo_jazz
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = JazzColors
}
