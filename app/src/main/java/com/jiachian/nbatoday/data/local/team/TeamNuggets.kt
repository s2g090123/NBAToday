package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamNuggets(
    override val teamId: Int = 1610612743,
    override val abbreviation: String = "DEN",
    override val teamName: String = "Nuggets",
    override val location: String = "Denver",
    override val logoRes: Int = R.drawable.ic_team_logo_nuggets,
    override val conference: Conference = Conference.WEST,
    override val division: Division = Division.NORTHWEST
) : DefaultTeam()
