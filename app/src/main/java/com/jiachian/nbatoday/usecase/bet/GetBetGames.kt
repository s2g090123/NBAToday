package com.jiachian.nbatoday.usecase.bet

import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.repository.bet.BetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class GetBetGames(
    private val repository: BetRepository,
) {
    operator fun invoke(account: String): Flow<List<BetAndGame>> {
        return repository.getBetGames(account)
            .mapLatest { games ->
                games.sortedBy { game ->
                    game.game.gameDateTime
                }
            }
    }
}
