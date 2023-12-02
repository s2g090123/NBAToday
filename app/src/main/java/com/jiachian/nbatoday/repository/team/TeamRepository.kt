package com.jiachian.nbatoday.repository.team

import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class TeamRepository : BaseRepository() {
    abstract suspend fun updateTeamStats()
    abstract suspend fun updateTeamStats(teamId: Int)
    abstract suspend fun updateTeamPlayers(teamId: Int)

    abstract fun getTeams(): Flow<List<Team>>
    abstract fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?>
    abstract fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int>
    abstract fun getTeamPointsRank(teamId: Int): Flow<Int>
    abstract fun getTeamReboundsRank(teamId: Int): Flow<Int>
    abstract fun getTeamAssistsRank(teamId: Int): Flow<Int>
    abstract fun getTeamPlusMinusRank(teamId: Int): Flow<Int>
}
