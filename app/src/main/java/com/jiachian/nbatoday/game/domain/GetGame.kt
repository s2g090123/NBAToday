package com.jiachian.nbatoday.game.domain

import com.jiachian.nbatoday.common.domain.Error
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.game.data.GameRepository
import com.jiachian.nbatoday.game.data.model.local.GameAndBets

enum class GetGameError : Error {
    GAME_NOT_FOUND
}

class GetGame(
    private val repository: GameRepository,
) {
    suspend operator fun invoke(gameId: String): Resource<GameAndBets, GetGameError> {
        return repository
            .getGameAndBets(gameId)
            .let { gameAndBets ->
                gameAndBets?.let {
                    Resource.Success(it)
                } ?: Resource.Error(GetGameError.GAME_NOT_FOUND)
            }
    }
}
