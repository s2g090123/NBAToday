package com.jiachian.nbatoday.team.domain

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.team.data.TeamRepository
import com.jiachian.nbatoday.team.domain.error.AddTeamsError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddTeams(
    private val repository: TeamRepository
) {
    operator fun invoke(): Flow<Resource<Unit, AddTeamsError>> = flow {
        emit(Resource.Loading())
        when (repository.addTeams()) {
            is Response.Error -> emit(Resource.Error(AddTeamsError.ADD_FAILED))
            is Response.Success -> emit(Resource.Success(Unit))
        }
    }
}
