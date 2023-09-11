package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamPistons(
    override val teamId: Int = 1610612765,
    override val abbreviation: String = "DET",
    override val teamName: String = "Pistons",
    override val location: String = "Detroit",
    override val logoRes: Int = R.drawable.ic_team_logo_pistons,
    override val conference: Conference = Conference.EAST,
    override val division: Division = Division.CENTRAL
) : DefaultTeam()
