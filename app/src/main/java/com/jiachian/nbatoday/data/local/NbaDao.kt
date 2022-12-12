package com.jiachian.nbatoday.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGames(games: List<NbaGame>)
}