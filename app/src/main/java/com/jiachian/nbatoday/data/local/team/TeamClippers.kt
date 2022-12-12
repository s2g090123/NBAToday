package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamClippers(
    override val teamId: Int = 1610612746,
    override val abbreviation: String = "LAC",
    override val teamName: String = "Clippers",
    override val location: String = "Los Angeles",
    override val logoRes: Int = R.drawable.ic_team_logo_clippers
) : DefaultTeam()