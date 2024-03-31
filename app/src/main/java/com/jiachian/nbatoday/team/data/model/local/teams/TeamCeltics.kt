package com.jiachian.nbatoday.team.data.model.local.teams

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.CelticsColors
import com.jiachian.nbatoday.common.ui.theme.NBAColors

val teamCeltics = object : NBATeam {
    override val teamId: Int = 1610612738
    override val abbreviation: String = "BOS"
    override val teamName: String = "Celtics"
    override val location: String = "Boston"
    override val logoRes: Int = R.drawable.ic_team_logo_celtics
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val colors: NBAColors = CelticsColors
}
