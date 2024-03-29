package com.jiachian.nbatoday.repository.team

import com.jiachian.nbatoday.CurrentSeason
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.database.dao.TeamDao
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.models.local.team.TeamRank
import com.jiachian.nbatoday.models.remote.team.extensions.toTeamPlayerStats
import com.jiachian.nbatoday.models.remote.team.extensions.toTeamStats
import com.jiachian.nbatoday.service.TeamService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull

class NBATeamRepository(
    private val dao: TeamDao,
    private val service: TeamService,
) : TeamRepository() {
    override suspend fun addTeams(): Response<Unit> {
        return service
            .getTeam(CurrentSeason, null)
            .takeIf { !it.isError() }
            ?.body()
            ?.toTeamStats()
            ?.also { teams ->
                dao.addTeams(teams)
            }
            ?.let { Response.Success(Unit) }
            ?: Response.Error()
    }

    override suspend fun updateTeamPlayers(teamId: Int): Response<Unit> {
        return service
            .getTeamPlayer(season = CurrentSeason, teamId = teamId)
            .takeIf { !it.isError() }
            ?.body()
            ?.toTeamPlayerStats()
            ?.let { remoteTeamPlayers ->
                deleteTradedPlayers(teamId, remoteTeamPlayers)
                dao.addTeamPlayers(remoteTeamPlayers)
            }
            ?.let { Response.Success(Unit) }
            ?: Response.Error()
    }

    override fun getTeams(conference: NBATeam.Conference): Flow<List<Team>> {
        return dao.getTeams(conference)
    }

    override fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?> {
        return dao.getTeamAndPlayers(teamId)
    }

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<TeamRank> {
        return combine(
            dao.getTeamStanding(teamId, conference),
            dao.getPointsRank(teamId),
            dao.getReboundsRank(teamId),
            dao.getAssistsRank(teamId),
            dao.getPlusMinusRank(teamId)
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

    private suspend fun deleteTradedPlayers(teamId: Int, remoteTeamPlayers: List<TeamPlayer>) {
        loading {
            val remoteTeamPlayerIds = remoteTeamPlayers.map { it.playerId }
            // delete players who were traded to another team
            dao
                .getTeamAndPlayers(teamId)
                .firstOrNull()
                ?.teamPlayers
                ?.map { teamPlayer -> teamPlayer.playerId }
                ?.filterNot { playerId -> playerId in remoteTeamPlayerIds }
                ?.also { playerIds ->
                    dao.deleteTeamPlayers(teamId, playerIds)
                }
        }
    }
}
