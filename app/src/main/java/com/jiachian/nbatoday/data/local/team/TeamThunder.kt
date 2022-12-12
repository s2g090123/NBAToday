package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamThunder(
    override val teamId: Int = 1610612760,
    override val abbreviation: String = "OKC",
    override val teamName: String = "Thunder",
    override val location: String = "Oklahoma City",
    override val logoRes: Int = R.drawable.ic_team_logo_thunder
) : DefaultTeam()