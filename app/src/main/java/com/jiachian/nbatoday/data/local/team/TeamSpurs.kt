package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamSpurs(
    override val teamId: Int = 1610612759,
    override val abbreviation: String = "SAS",
    override val teamName: String = "Spurs",
    override val location: String = "San Antonio",
    override val logoRes: Int = R.drawable.ic_team_logo_spurs
) : DefaultTeam()