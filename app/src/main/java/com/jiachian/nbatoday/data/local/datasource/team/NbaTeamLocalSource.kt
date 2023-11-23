package com.jiachian.nbatoday.data.local.datasource.team

import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.dao.TeamDao
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import kotlinx.coroutines.flow.Flow

class NbaTeamLocalSource(
    private val teamDao: TeamDao,
) : TeamLocalSource() {
    override fun getTeamStats(): Flow<List<TeamStats>> {
        return teamDao.getTeamStats()
    }

    override fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?> {
        return teamDao.getTeamAndPlayerStats(teamId)
    }

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int> {
        return teamDao.getTeamRank(teamId, conference)
    }

    override fun getTeamPointsRank(teamId: Int): Flow<Int> {
        return teamDao.getPointsRank(teamId)
    }

    override fun getTeamReboundsRank(teamId: Int): Flow<Int> {
        return teamDao.getReboundsRank(teamId)
    }

    override fun getTeamAssistsRank(teamId: Int): Flow<Int> {
        return teamDao.getAssistsRank(teamId)
    }

    override fun getTeamPlusMinusRank(teamId: Int): Flow<Int> {
        return teamDao.getPlusMinusRank(teamId)
    }

    override suspend fun updateTeamStats(stats: TeamStats) {
        teamDao.insertTeamStats(stats)
    }

    override suspend fun updateTeamStats(stats: List<TeamStats>) {
        teamDao.insertTeamStats(stats)
    }

    override suspend fun updatePlayerStats(stats: List<PlayerStats>) {
        teamDao.insertPlayerStats(stats)
    }

    override suspend fun deletePlayerStats(teamId: Int, playerIds: List<Int>) {
        teamDao.deleteTeamPlayersStats(teamId, playerIds)
    }
}
