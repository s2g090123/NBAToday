package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamCavaliers(
    override val teamId: Int = 1610612739,
    override val abbreviation: String = "CLE",
    override val teamName: String = "Cavaliers",
    override val location: String = "Cleveland",
    override val logoRes: Int = R.drawable.ic_team_logo_cavaliers,
    override val conference: Conference = Conference.EAST,
    override val division: Division = Division.CENTRAL
) : DefaultTeam()
