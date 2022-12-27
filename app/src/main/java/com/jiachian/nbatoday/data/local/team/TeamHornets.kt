package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamHornets(
    override val teamId: Int = 1610612766,
    override val abbreviation: String = "CHA",
    override val teamName: String = "Hornets",
    override val location: String = "Charlotte",
    override val logoRes: Int = R.drawable.ic_team_logo_hornets,
    override val conference: Conference = Conference.EAST,
    override val division: Division = Division.SOUTHEAST
) : DefaultTeam()