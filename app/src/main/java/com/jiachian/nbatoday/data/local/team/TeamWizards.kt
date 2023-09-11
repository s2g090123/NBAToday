package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamWizards(
    override val teamId: Int = 1610612764,
    override val abbreviation: String = "WAS",
    override val teamName: String = "Wizards",
    override val location: String = "Washington",
    override val logoRes: Int = R.drawable.ic_team_logo_wizards,
    override val conference: Conference = Conference.EAST,
    override val division: Division = Division.SOUTHEAST
) : DefaultTeam()
