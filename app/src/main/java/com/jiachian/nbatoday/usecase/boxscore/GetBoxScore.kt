package com.jiachian.nbatoday.usecase.boxscore

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import com.jiachian.nbatoday.repository.game.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class GetBoxScoreError : Error {
    NOT_FOUND
}

class GetBoxScore(
    private val repository: GameRepository
) {
    operator fun invoke(gameId: String): Flow<Resource<BoxScoreAndGame, GetBoxScoreError>> {
        return repository.getBoxScoreAndGame(gameId)
            .map { game ->
                game?.let {
                    Resource.Success(it)
                } ?: Resource.Error(GetBoxScoreError.NOT_FOUND)
            }
    }
}
