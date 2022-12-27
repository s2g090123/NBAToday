package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamJazz(
    override val teamId: Int = 1610612762,
    override val abbreviation: String = "UTA",
    override val teamName: String = "Jazz",
    override val location: String = "Utah",
    override val logoRes: Int = R.drawable.ic_team_logo_jazz,
    override val conference: Conference = Conference.WEST,
    override val division: Division = Division.NORTHWEST
) : DefaultTeam()