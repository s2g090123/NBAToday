package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.database.dao.TeamDao
import com.jiachian.nbatoday.datasource.local.team.TeamLocalSource
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.models.local.team.TeamRank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class TestTeamLocalSource(
    private val teamDao: TeamDao
) : TeamLocalSource() {
    override fun getTeams(conference: NBATeam.Conference): Flow<List<Team>> {
        return teamDao.getTeams(conference)
    }

    override fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?> {
        return teamDao.getTeamAndPlayers(teamId)
    }

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<TeamRank> {
        return combine(
            teamDao.getTeamStanding(teamId, conference),
            teamDao.getPointsRank(teamId),
            teamDao.getReboundsRank(teamId),
            teamDao.getAssistsRank(teamId),
            teamDao.getPlusMinusRank(teamId)
        ) { standing, points, rebounds, assists, plusMinus ->
            TeamRank(
                standing = standing,
                pointsRank = points,
                reboundsRank = rebounds,
                assistsRank = assists,
                plusMinusRank = plusMinus
            )
        }
    }

    override suspend fun insertTeams(teams: List<Team>) {
        teamDao.addTeams(teams)
    }

    override suspend fun insertTeamPlayers(teamPlayers: List<TeamPlayer>) {
        teamDao.addTeamPlayers(teamPlayers)
    }

    override suspend fun deleteTeamPlayers(teamId: Int, playerIds: List<Int>) {
        teamDao.deleteTeamPlayers(teamId, playerIds)
    }
}
