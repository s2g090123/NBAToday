package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamBlazers(
    override val teamId: Int = 1610612757,
    override val abbreviation: String = "POR",
    override val teamName: String = "Blazers",
    override val location: String = "Portland",
    override val logoRes: Int = R.drawable.ic_team_logo_blazers,
    override val conference: Conference = Conference.WEST,
    override val division: Division = Division.NORTHWEST
) : DefaultTeam()