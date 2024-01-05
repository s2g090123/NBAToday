package com.jiachian.nbatoday.models.local.team.data

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.NuggetsColors
import com.jiachian.nbatoday.models.local.team.NBATeam

val teamNuggets = object : NBATeam {
    override val teamId: Int = 1610612743
    override val abbreviation: String = "DEN"
    override val teamName: String = "Nuggets"
    override val location: String = "Denver"
    override val logoRes: Int = R.drawable.ic_team_logo_nuggets
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val colors: NBAColors = NuggetsColors
}
