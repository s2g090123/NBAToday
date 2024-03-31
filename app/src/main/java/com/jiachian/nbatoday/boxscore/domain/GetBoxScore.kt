package com.jiachian.nbatoday.boxscore.domain

import com.jiachian.nbatoday.boxscore.data.model.local.BoxScoreAndGame
import com.jiachian.nbatoday.boxscore.domain.error.GetBoxScoreError
import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.game.data.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
