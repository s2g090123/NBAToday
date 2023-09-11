package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamKnicks(
    override val teamId: Int = 1610612752,
    override val abbreviation: String = "NYK",
    override val teamName: String = "Knicks",
    override val location: String = "New York",
    override val logoRes: Int = R.drawable.ic_team_logo_knicks,
    override val conference: Conference = Conference.EAST,
    override val division: Division = Division.ATLANTIC
) : DefaultTeam()
