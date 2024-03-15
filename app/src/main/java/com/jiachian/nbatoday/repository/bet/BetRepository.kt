package com.jiachian.nbatoday.repository.bet

import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

/**
 * Manage bet-related to games.
 */
abstract class BetRepository : BaseRepository() {
    /**
     * Inserts a new bet for a specified game.
     *
     * @param gameId The ID of the game for which the bet is placed.
     * @param homePoints The points predicted for the home team.
     * @param awayPoints The points predicted for the away team.
     */
    abstract suspend fun addBet(gameId: String, account: String, homePoints: Long, awayPoints: Long)

    /**
     * Deletes a bet from the repository.
     *
     * @param bet The bet to be deleted.
     */
    abstract suspend fun deleteBet(bet: Bet)

    /**
     * Retrieves a flow of bets and associated games for a specific user account.
     *
     * @param account The user account for which to retrieve bets.
     * @return A Flow emitting a list of BetAndGame objects.
     */
    abstract fun getBetGames(account: String): Flow<List<BetAndGame>>
}
