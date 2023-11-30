package com.jiachian.nbatoday.datasource.local.team

import com.jiachian.nbatoday.database.dao.TeamDao
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import kotlinx.coroutines.flow.Flow

class NBATeamLocalSource(
    private val teamDao: TeamDao,
) : TeamLocalSource() {
    override fun getTeams(): Flow<List<Team>> {
        return teamDao.getTeams()
    }

    override fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?> {
        return teamDao.getTeamAndPlayers(teamId)
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

    override suspend fun updateTeams(stats: List<Team>) {
        teamDao.insertTeams(stats)
    }

    override suspend fun updateTeamPlayers(stats: List<TeamPlayer>) {
        teamDao.insertTeamPlayers(stats)
    }

    override suspend fun deleteTeamPlayers(teamId: Int, playerIds: List<Int>) {
        teamDao.deleteTeamPlayers(teamId, playerIds)
    }
}
