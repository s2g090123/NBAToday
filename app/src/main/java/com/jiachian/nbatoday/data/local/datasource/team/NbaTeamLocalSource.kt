package com.jiachian.nbatoday.data.local.datasource.team

import com.jiachian.nbatoday.data.local.NbaDao
import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import kotlinx.coroutines.flow.Flow

class NbaTeamLocalSource(
    private val dao: NbaDao,
) : TeamLocalSource() {
    override fun getTeamStats(): Flow<List<TeamStats>> {
        return dao.getTeamStats()
    }

    override fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?> {
        return dao.getTeamAndPlayerStats(teamId)
    }

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int> {
        return dao.getRank(teamId, conference)
    }

    override fun getTeamPointsRank(teamId: Int): Flow<Int> {
        return dao.getPointsRank(teamId)
    }

    override fun getTeamReboundsRank(teamId: Int): Flow<Int> {
        return dao.getReboundsRank(teamId)
    }

    override fun getTeamAssistsRank(teamId: Int): Flow<Int> {
        return dao.getAssistsRank(teamId)
    }

    override fun getTeamPlusMinusRank(teamId: Int): Flow<Int> {
        return dao.getPlusMinusRank(teamId)
    }

    override suspend fun updateTeamStats(stats: TeamStats) {
        dao.insertTeamStats(stats)
    }

    override suspend fun updateTeamStats(stats: List<TeamStats>) {
        dao.insertTeamStats(stats)
    }

    override suspend fun updatePlayerStats(stats: List<PlayerStats>) {
        dao.insertPlayerStats(stats)
    }

    override suspend fun deletePlayerStats(teamId: Int, playerIds: List<Int>) {
        dao.deleteTeamPlayersStats(teamId, playerIds)
    }
}
