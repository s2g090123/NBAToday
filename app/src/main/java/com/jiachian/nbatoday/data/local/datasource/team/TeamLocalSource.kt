package com.jiachian.nbatoday.data.local.datasource.team

import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
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
    abstract suspend fun updatePlayerStats(stats: List<PlayerStats>)
    abstract suspend fun deletePlayerStats(teamId: Int, playerIds: List<Int>)
}
