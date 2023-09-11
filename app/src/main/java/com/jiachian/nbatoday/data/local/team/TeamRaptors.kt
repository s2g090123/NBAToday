package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamRaptors(
    override val teamId: Int = 1610612761,
    override val abbreviation: String = "TOR",
    override val teamName: String = "Raptors",
    override val location: String = "Toronto",
    override val logoRes: Int = R.drawable.ic_team_logo_raptors,
    override val conference: Conference = Conference.EAST,
    override val division: Division = Division.ATLANTIC
) : DefaultTeam()
