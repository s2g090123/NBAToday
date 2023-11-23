package com.jiachian.nbatoday.data.repository.team

import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class TeamRepository : BaseRepository() {
    abstract suspend fun refreshTeamStats(teamId: Int?)
    abstract suspend fun refreshTeamPlayersStats(teamId: Int)

    abstract fun getTeamStats(): Flow<List<TeamStats>>
    abstract fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?>
    abstract fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int>
    abstract fun getTeamPointsRank(teamId: Int): Flow<Int>
    abstract fun getTeamReboundsRank(teamId: Int): Flow<Int>
    abstract fun getTeamAssistsRank(teamId: Int): Flow<Int>
    abstract fun getTeamPlusMinusRank(teamId: Int): Flow<Int>
}
