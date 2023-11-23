package com.jiachian.nbatoday.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiachian.nbatoday.data.local.BetAndNbaGame
import com.jiachian.nbatoday.data.local.bet.Bets
import kotlinx.coroutines.flow.Flow

@Dao
interface BetDao {
    @Query("SELECT * FROM nba_game_bets")
    fun getBetsAndGames(): Flow<List<BetAndNbaGame>>

    @Query("SELECT * FROM nba_game_bets WHERE bets_account == :account")
    fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBet(bet: Bets)

    @Delete
    suspend fun deleteBet(bet: Bets)
}
