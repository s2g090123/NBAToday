package com.jiachian.nbatoday.datasource.local.team

import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayerStats
import com.jiachian.nbatoday.models.local.team.TeamStats
import kotlinx.coroutines.flow.Flow

abstract class TeamLocalSource {
    abstract fun getTeamStats(): Flow<List<TeamStats>>
    abstract fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?>
    abstract fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int>
    abstract fun getTeamPointsRank(teamId: Int): Flow<Int>
    abstract fun getTeamReboundsRank(teamId: Int): Flow<Int>
    abstract fun getTeamAssistsRank(teamId: Int): Flow<Int>
    abstract fun getTeamPlusMinusRank(teamId: Int): Flow<Int>

    abstract suspend fun updateTeamStats(stats: List<TeamStats>)
    abstract suspend fun updatePlayerStats(stats: List<TeamPlayerStats>)
    abstract suspend fun deletePlayerStats(teamId: Int, playerIds: List<Int>)
}
