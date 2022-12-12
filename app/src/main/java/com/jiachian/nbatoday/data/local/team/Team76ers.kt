package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class Team76ers(
    override val teamId: Int = 1610612755,
    override val abbreviation: String = "PHI",
    override val teamName: String = "76ers",
    override val location: String = "Philadelphia",
    override val logoRes: Int = R.drawable.ic_team_logo_76ers
) : DefaultTeam()