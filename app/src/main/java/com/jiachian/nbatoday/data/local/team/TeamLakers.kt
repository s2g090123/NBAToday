package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamLakers(
    override val teamId: Int = 1610612747,
    override val abbreviation: String = "LAL",
    override val teamName: String = "Lakers",
    override val location: String = "Los Angeles",
    override val logoRes: Int = R.drawable.ic_team_logo_lakers,
    override val conference: Conference = Conference.WEST,
    override val division: Division = Division.PACIFIC
) : DefaultTeam()
