package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamPacers(
    override val teamId: Int = 1610612754,
    override val abbreviation: String = "IND",
    override val teamName: String = "Pacers",
    override val location: String = "Indiana",
    override val logoRes: Int = R.drawable.ic_team_logo_pacers
) : DefaultTeam()