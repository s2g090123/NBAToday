package com.jiachian.nbatoday.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import kotlinx.coroutines.flow.Flow

/**
 * Handle operations related to the Bet entity in the database.
 */
@Dao
interface BetDao {
    /**
     * Retrieves a flow of all bets and associated games from the database.
     *
     * @return Flow emitting a list of [BetAndGame].
     */
    @Query("SELECT * FROM bet")
    fun getBetsAndGames(): Flow<List<BetAndGame>>

    /**
     * Retrieves a flow of bets and associated games for a specific account from the database.
     *
     * @param account The account for which to retrieve bets.
     * @return Flow emitting a list of [BetAndGame].
     */
    @Query("SELECT * FROM bet WHERE bet_account == :account")
    fun getBetsAndGames(account: String): Flow<List<BetAndGame>>

    /**
     * Inserts a new bet into the database, ignoring conflicts.
     *
     * @param bet The [Bet] object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBet(bet: Bet)

    /**
     * Deletes a bet from the database.
     *
     * @param bet The [Bet] object to be deleted.
     */
    @Delete
    suspend fun deleteBet(bet: Bet)
}
