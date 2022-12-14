package com.jiachian.nbatoday.data.local

import androidx.room.*
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface NbaDao {

    /** NBA GameInfo */
    @Query("SELECT * FROM nba_game")
    fun getGames(): Flow<List<NbaGame>>

    @Query("SELECT * FROM nba_game WHERE game_date >= :from AND game_date <= :to")
    fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>

    @Query("SELECT DISTINCT game_date FROM nba_game")
    fun getDates(): Flow<List<Date>>

    @Query("SELECT EXISTS (SELECT 1 FROM nba_game)")
    fun exitsData(): Boolean

    @Insert(entity = NbaGame::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<NbaGame>)

    @Update(entity = NbaGame::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGame(status: List<GameUpdateData>)
}