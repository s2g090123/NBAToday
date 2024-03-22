package com.jiachian.nbatoday.usecase.team

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.repository.team.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

enum class AddTeamsError : Error {
    ADD_FAILED
}

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
