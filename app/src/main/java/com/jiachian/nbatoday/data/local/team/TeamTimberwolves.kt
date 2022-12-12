package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamTimberwolves(
    override val teamId: Int = 1610612750,
    override val abbreviation: String = "MIN",
    override val teamName: String = "Timberwolves",
    override val location: String = "Minnesota",
    override val logoRes: Int = R.drawable.ic_team_logo_timberwolves
) : DefaultTeam()