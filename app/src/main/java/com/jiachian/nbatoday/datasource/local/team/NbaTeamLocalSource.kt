package com.jiachian.nbatoday.datasource.local.team

import com.jiachian.nbatoday.database.dao.TeamDao
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import kotlinx.coroutines.flow.Flow

class NbaTeamLocalSource(
    private val teamDao: TeamDao,
) : TeamLocalSource() {
    override fun getTeamStats(): Flow<List<Team>> {
        return teamDao.getTeams()
    }

    override fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?> {
        return teamDao.getTeamsAndPlayers(teamId)
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

    override suspend fun updateTeamStats(stats: List<Team>) {
        teamDao.insertTeams(stats)
    }

    override suspend fun updatePlayerStats(stats: List<TeamPlayer>) {
        teamDao.insertTeamsAndPlayers(stats)
    }

    override suspend fun deletePlayerStats(teamId: Int, playerIds: List<Int>) {
        teamDao.deleteTeamPlayers(teamId, playerIds)
    }
}
