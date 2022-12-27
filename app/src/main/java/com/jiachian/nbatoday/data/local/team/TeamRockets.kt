package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamRockets(
    override val teamId: Int = 1610612745,
    override val abbreviation: String = "HOU",
    override val teamName: String = "Rockets",
    override val location: String = "Houston",
    override val logoRes: Int = R.drawable.ic_team_logo_rockets,
    override val conference: Conference = Conference.WEST,
    override val division: Division = Division.SOUTHWEST
) : DefaultTeam()