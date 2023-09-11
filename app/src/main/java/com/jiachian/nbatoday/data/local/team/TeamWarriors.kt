package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamWarriors(
    override val teamId: Int = 1610612744,
    override val abbreviation: String = "GSW",
    override val teamName: String = "Warriors",
    override val location: String = "Golden State",
    override val logoRes: Int = R.drawable.ic_team_logo_warriors,
    override val conference: Conference = Conference.WEST,
    override val division: Division = Division.PACIFIC
) : DefaultTeam()
