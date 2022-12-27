package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamPelicans(
    override val teamId: Int = 1610612740,
    override val abbreviation: String = "NOP",
    override val teamName: String = "Pelicans",
    override val location: String = "New Orleans",
    override val logoRes: Int = R.drawable.ic_team_logo_pelicans,
    override val conference: Conference = Conference.WEST,
    override val division: Division = Division.SOUTHWEST,
) : DefaultTeam()