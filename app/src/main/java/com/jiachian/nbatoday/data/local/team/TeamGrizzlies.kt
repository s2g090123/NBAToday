package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamGrizzlies(
    override val teamId: Int = 1610612763,
    override val abbreviation: String = "MEM",
    override val teamName: String = "Grizzlies",
    override val location: String = "Memphis",
    override val logoRes: Int = R.drawable.ic_team_logo_grizzlies,
    override val conference: Conference = Conference.WEST,
    override val division: Division = Division.SOUTHWEST
) : DefaultTeam()