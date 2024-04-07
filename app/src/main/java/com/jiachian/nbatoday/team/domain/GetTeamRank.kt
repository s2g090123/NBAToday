package com.jiachian.nbatoday.team.domain

import com.jiachian.nbatoday.team.data.TeamRepository
import com.jiachian.nbatoday.team.data.model.local.TeamRank
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import kotlinx.coroutines.flow.Flow

class GetTeamRank(
    private val repository: TeamRepository
) {
    operator fun invoke(teamId: Int, conference: NBATeam.Conference): Flow<TeamRank> {
        return repository.getTeamRank(teamId, conference)
    }
}
