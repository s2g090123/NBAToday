package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamHeat(
    override val teamId: Int = 1610612748,
    override val abbreviation: String = "MIA",
    override val teamName: String = "Heat",
    override val location: String = "Miami",
    override val logoRes: Int = R.drawable.ic_team_logo_heat
) : DefaultTeam()