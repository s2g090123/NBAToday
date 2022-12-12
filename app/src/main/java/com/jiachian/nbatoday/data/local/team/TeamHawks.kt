package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamHawks(
    override val teamId: Int = 1610612737,
    override val abbreviation: String = "ATL",
    override val teamName: String = "Hawks",
    override val location: String = "Atlanta",
    override val logoRes: Int = R.drawable.ic_team_logo_hawks
) : DefaultTeam()