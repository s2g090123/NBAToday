package com.jiachian.nbatoday.team.domain

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.team.data.TeamRepository
import com.jiachian.nbatoday.team.domain.error.UpdateTeamInfoError
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateTeamInfo(
    private val repository: TeamRepository,
) {
    suspend operator fun invoke(teamId: Int): Flow<Resource<Unit, UpdateTeamInfoError>> = flow {
        emit(Resource.Loading())
        val (addTeamResponse, updateTeamPlayersResponse) = coroutineScope {
            val addTeamsDeferred = async { repository.addTeams() }
            val updateTeamPlayersDeferred = async { repository.updateTeamPlayers(teamId) }
            addTeamsDeferred.await() to updateTeamPlayersDeferred.await()
        }
        when {
            addTeamResponse is Response.Error -> {
                emit(Resource.Error(UpdateTeamInfoError.UPDATE_TEAMS_FAILED))
            }
            updateTeamPlayersResponse is Response.Error -> {
                emit(Resource.Error(UpdateTeamInfoError.UPDATE_PLAYERS_FAILED))
            }
            else -> {
                emit(Resource.Success(Unit))
            }
        }
    }
}
