package com.jiachian.nbatoday.usecase.game

import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.repository.game.GameRepository
import kotlinx.coroutines.withContext

class GetGame(
    private val repository: GameRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) {
    suspend operator fun invoke(gameId: String) = withContext(dispatcherProvider.io) {
        repository.getGameAndBets(gameId)
    }
}
