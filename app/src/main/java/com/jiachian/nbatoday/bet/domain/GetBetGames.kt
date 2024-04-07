package com.jiachian.nbatoday.bet.domain

import com.jiachian.nbatoday.bet.data.BetRepository
import com.jiachian.nbatoday.bet.data.model.local.BetAndGame
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
