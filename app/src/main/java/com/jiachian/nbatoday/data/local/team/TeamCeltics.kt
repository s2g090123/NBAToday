package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.R

data class TeamCeltics(
    override val teamId: Int = 1610612738,
    override val abbreviation: String = "BOS",
    override val teamName: String = "Celtics",
    override val location: String = "Boston",
    override val logoRes: Int = R.drawable.ic_team_logo_celtics,
    override val conference: Conference = Conference.EAST,
    override val division: Division = Division.ATLANTIC
) : DefaultTeam()
