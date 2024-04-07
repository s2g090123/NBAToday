package com.jiachian.nbatoday.boxscore.domain

import com.jiachian.nbatoday.boxscore.domain.error.AddBoxScoreError
import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.game.data.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
