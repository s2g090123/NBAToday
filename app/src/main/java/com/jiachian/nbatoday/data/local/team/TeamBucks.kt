package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamBucks(
    override val teamId: Int = 1610612749,
    override val abbreviation: String = "MIL",
    override val teamName: String = "Bucks",
    override val location: String = "Milwaukee",
    override val logoRes: Int = R.drawable.ic_team_logo_bucks
) : DefaultTeam()