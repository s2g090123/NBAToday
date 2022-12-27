package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamBulls(
    override val teamId: Int = 1610612741,
    override val abbreviation: String = "CHI",
    override val teamName: String = "Bulls",
    override val location: String = "Chicago",
    override val logoRes: Int = R.drawable.ic_team_logo_bulls,
    override val conference: Conference = Conference.EAST,
    override val division: Division = Division.CENTRAL
) : DefaultTeam()