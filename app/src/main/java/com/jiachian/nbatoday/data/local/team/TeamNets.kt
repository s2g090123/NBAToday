package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamNets(
    override val teamId: Int = 1610612751,
    override val abbreviation: String = "BKN",
    override val teamName: String = "Nets",
    override val location: String = "Brooklyn",
    override val logoRes: Int = R.drawable.ic_team_logo_nets
) : DefaultTeam()