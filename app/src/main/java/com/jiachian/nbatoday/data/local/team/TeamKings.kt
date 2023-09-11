package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamKings(
    override val teamId: Int = 1610612758,
    override val abbreviation: String = "SAC",
    override val teamName: String = "Kings",
    override val location: String = "Sacramento",
    override val logoRes: Int = R.drawable.ic_team_logo_kings,
    override val conference: Conference = Conference.WEST,
    override val division: Division = Division.PACIFIC
) : DefaultTeam()
