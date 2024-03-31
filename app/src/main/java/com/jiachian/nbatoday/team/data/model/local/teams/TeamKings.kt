package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.KingsColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamKings = object : NBATeam {
    override val teamId: Int = 1610612758
    override val abbreviation: String = "SAC"
    override val teamName: String = "Kings"
    override val location: String = "Sacramento"
    override val logoRes: Int = R.drawable.ic_team_logo_kings
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = KingsColors
}
