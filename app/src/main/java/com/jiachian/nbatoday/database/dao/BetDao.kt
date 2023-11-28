package com.jiachian.nbatoday.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.bet.BetAndNbaGame
import kotlinx.coroutines.flow.Flow

@Dao
interface BetDao {
    @Query("SELECT * FROM bet")
    fun getBetsAndGames(): Flow<List<BetAndNbaGame>>

    @Query("SELECT * FROM bet WHERE bet_account == :account")
    fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBet(bet: Bet)

    @Delete
    suspend fun deleteBet(bet: Bet)
}
