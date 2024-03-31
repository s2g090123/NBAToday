package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.datasource.local.team.TeamLocalSource
import com.jiachian.nbatoday.team.data.TeamDao
import com.jiachian.nbatoday.team.data.model.local.Team
import com.jiachian.nbatoday.team.data.model.local.TeamAndPlayers
import com.jiachian.nbatoday.team.data.model.local.TeamPlayer
import com.jiachian.nbatoday.team.data.model.local.TeamRank
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
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
