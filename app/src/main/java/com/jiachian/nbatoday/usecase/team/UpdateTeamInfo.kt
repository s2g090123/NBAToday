package com.jiachian.nbatoday.usecase.team

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.repository.team.TeamRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

enum class UpdateTeamInfoError : Error {
    UPDATE_TEAMS_FAILED,
    UPDATE_PLAYERS_FAILED,
}

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
