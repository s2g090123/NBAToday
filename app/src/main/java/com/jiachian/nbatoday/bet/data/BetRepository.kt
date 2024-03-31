package com.jiachian.nbatoday.bet.data

import com.jiachian.nbatoday.bet.data.model.local.Bet
import com.jiachian.nbatoday.bet.data.model.local.BetAndGame
import kotlinx.coroutines.flow.Flow

/**
 * Manage bet-related to games.
 */
interface BetRepository {
    /**
     * Inserts a new bet for a specified game.
     */
    suspend fun addBet(bet: Bet)

    /**
     * Deletes a bet from the repository.
     *
     * @param bet The bet to be deleted.
     */
    suspend fun deleteBet(bet: Bet)

    /**
     * Retrieves a flow of bets and associated games for a specific user account.
     *
     * @param account The user account for which to retrieve bets.
     * @return A Flow emitting a list of BetAndGame objects.
     */
    fun getBetGames(account: String): Flow<List<BetAndGame>>
}
