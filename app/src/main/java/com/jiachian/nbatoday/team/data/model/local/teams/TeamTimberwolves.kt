package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.common.ui.theme.TimberwolvesColors

val teamTimberwolves = object : NBATeam {
    override val teamId: Int = 1610612750
    override val abbreviation: String = "MIN"
    override val teamName: String = "Timberwolves"
    override val location: String = "Minnesota"
    override val logoRes: Int = R.drawable.ic_team_logo_timberwolves
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = TimberwolvesColors
}
