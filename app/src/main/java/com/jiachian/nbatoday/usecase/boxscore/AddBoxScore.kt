package com.jiachian.nbatoday.usecase.boxscore

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.repository.game.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

enum class AddBoxScoreError : Error {
    ADD_FAILED
}

class AddBoxScore(
    private val repository: GameRepository
) {
    suspend operator fun invoke(gameId: String): Flow<Resource<Unit, AddBoxScoreError>> = flow {
        emit(Resource.Loading())
        when (repository.addBoxScore(gameId)) {
            is Response.Error -> emit(Resource.Error(AddBoxScoreError.ADD_FAILED))
            is Response.Success -> emit(Resource.Success(Unit))
        }
    }
}
