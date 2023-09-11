package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamMagic(
    override val teamId: Int = 1610612753,
    override val abbreviation: String = "ORL",
    override val teamName: String = "Magic",
    override val location: String = "Orlando",
    override val logoRes: Int = R.drawable.ic_team_logo_magic,
    override val conference: Conference = Conference.EAST,
    override val division: Division = Division.SOUTHEAST
) : DefaultTeam()
