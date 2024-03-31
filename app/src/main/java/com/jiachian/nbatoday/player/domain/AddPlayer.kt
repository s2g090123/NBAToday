package com.jiachian.nbatoday.player.domain

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.player.data.PlayerRepository
import com.jiachian.nbatoday.player.domain.error.AddPlayerError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
