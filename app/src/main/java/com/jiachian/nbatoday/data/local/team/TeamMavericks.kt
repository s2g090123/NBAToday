package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamMavericks(
    override val teamId: Int = 1610612742,
    override val abbreviation: String = "DAL",
    override val teamName: String = "Mavericks",
    override val location: String = "Dallas",
    override val logoRes: Int = R.drawable.ic_team_logo_mavericks
) : DefaultTeam()