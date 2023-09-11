package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamSuns(
    override val teamId: Int = 1610612756,
    override val abbreviation: String = "PHX",
    override val teamName: String = "Suns",
    override val location: String = "Phoenix",
    override val logoRes: Int = R.drawable.ic_team_logo_suns,
    override val conference: Conference = Conference.WEST,
    override val division: Division = Division.PACIFIC
) : DefaultTeam()
