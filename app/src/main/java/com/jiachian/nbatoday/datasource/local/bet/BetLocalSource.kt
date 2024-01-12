package com.jiachian.nbatoday.datasource.local.bet

import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import kotlinx.coroutines.flow.Flow

/**
 * The local data source for bet-related operations.
 */
abstract class BetLocalSource {
    /**
     * Retrieves a flow of [BetAndGame] objects associated with a specific account.
     *
     * @param account The account for which to retrieve bets and games.
     * @return A [Flow] emitting a list of [BetAndGame] objects.
     */
    abstract fun getBetsAndGames(account: String): Flow<List<BetAndGame>>

    /**
     * Inserts a [Bet] object into the local data source.
     *
     * @param bet The [Bet] object to be inserted.
     */
    abstract suspend fun insertBet(bet: Bet)

    /**
     * Deletes a [Bet] object from the local data source.
     *
     * @param bet The [Bet] object to be deleted.
     */
    abstract suspend fun deleteBet(bet: Bet)
}
