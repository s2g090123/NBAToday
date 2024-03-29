package com.jiachian.nbatoday.usecase.team

import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamRank
import com.jiachian.nbatoday.repository.team.TeamRepository
import kotlinx.coroutines.flow.Flow

class GetTeamRank(
    private val repository: TeamRepository
) {
    operator fun invoke(teamId: Int, conference: NBATeam.Conference): Flow<TeamRank> {
        return repository.getTeamRank(teamId, conference)
    }
}
