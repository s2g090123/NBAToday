package com.jiachian.nbatoday.usecase.player

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.repository.player.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

enum class AddPlayerError : Error {
    ADD_FAILED
}

class AddPlayer(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(playerId: Int): Flow<Resource<Unit, AddPlayerError>> = flow {
        emit(Resource.Loading())
        try {
            when (repository.addPlayer(playerId)) {
                is Response.Error -> emit(Resource.Error(AddPlayerError.ADD_FAILED))
                is Response.Success -> emit(Resource.Success(Unit))
            }
        } catch (e: Exception) {
            emit(Resource.Error(AddPlayerError.ADD_FAILED))
        }
    }
}
