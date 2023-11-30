package com.jiachian.nbatoday.datasource.local.team

import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import kotlinx.coroutines.flow.Flow

abstract class TeamLocalSource {
    abstract fun getTeams(): Flow<List<Team>>
    abstract fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?>
    abstract fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int>
    abstract fun getTeamPointsRank(teamId: Int): Flow<Int>
    abstract fun getTeamReboundsRank(teamId: Int): Flow<Int>
    abstract fun getTeamAssistsRank(teamId: Int): Flow<Int>
    abstract fun getTeamPlusMinusRank(teamId: Int): Flow<Int>

    abstract suspend fun updateTeams(stats: List<Team>)
    abstract suspend fun updateTeamPlayers(stats: List<TeamPlayer>)
    abstract suspend fun deleteTeamPlayers(teamId: Int, playerIds: List<Int>)
}
