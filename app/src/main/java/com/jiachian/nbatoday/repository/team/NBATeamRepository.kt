package com.jiachian.nbatoday.repository.team

import com.jiachian.nbatoday.datasource.local.team.TeamLocalSource
import com.jiachian.nbatoday.datasource.remote.team.TeamRemoteSource
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.models.local.team.TeamRank
import com.jiachian.nbatoday.models.remote.team.toTeamPlayerStats
import com.jiachian.nbatoday.models.remote.team.toTeamStats
import com.jiachian.nbatoday.utils.showErrorToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NBATeamRepository(
    private val teamLocalSource: TeamLocalSource,
    private val teamRemoteSource: TeamRemoteSource,
) : TeamRepository() {
    override suspend fun updateTeamStats() {
        loading {
            teamRemoteSource
                .getTeamStats()
                .takeIf { !it.isError() }
                ?.body()
                ?.toTeamStats()
                ?.let { teams ->
                    teamLocalSource.updateTeams(teams)
                }
                ?: showErrorToast()
        }
    }

    override suspend fun updateTeamStats(teamId: Int) {
        loading {
            teamRemoteSource
                .getTeamStats(teamId)
                .takeIf { !it.isError() }
                ?.body()
                ?.toTeamStats()
                ?.let { teams ->
                    teamLocalSource.updateTeams(teams)
                }
                ?: showErrorToast()
        }
    }

    override suspend fun updateTeamPlayers(teamId: Int) {
        loading {
            teamRemoteSource
                .getTeamPlayerStats(teamId)
                .takeIf { !it.isError() }
                ?.body()
                ?.toTeamPlayerStats()
                ?.let { remoteTeamPlayers ->
                    deleteTradedPlayers(teamId, remoteTeamPlayers)
                    teamLocalSource.updateTeamPlayers(remoteTeamPlayers)
                }
                ?: showErrorToast()
        }
    }

    override fun getTeams(conference: NBATeam.Conference): Flow<List<Team>> {
        return teamLocalSource.getTeams(conference)
    }

    override fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?> {
        return teamLocalSource.getTeamAndPlayers(teamId)
    }

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<TeamRank> {
        return teamLocalSource.getTeamRank(teamId, conference)
    }

    private suspend fun deleteTradedPlayers(teamId: Int, remoteTeamPlayers: List<TeamPlayer>) {
        loading {
            val remoteTeamPlayerIds = remoteTeamPlayers.map { it.playerId }
            // delete players who were traded to another team
            teamLocalSource
                .getTeamAndPlayers(teamId)
                .firstOrNull()
                ?.teamPlayers
                ?.map { teamPlayer -> teamPlayer.playerId }
                ?.filterNot { playerId -> playerId in remoteTeamPlayerIds }
                ?.also { playerIds ->
                    teamLocalSource.deleteTeamPlayers(teamId, playerIds)
                }
        }
    }
}
