package com.itrustmachines.nbatoday.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NbaDao {

    /** NBA GameInfo */
    @Query("SELECT * FROM nba_game")
    fun getGames(): Flow<List<NbaGame>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGames(games: List<NbaGame>)

    /** NBA GameDate */
    @Query("SELECT DISTINCT game_date FROM nba_game")
    fun getDates(): Flow<List<NbaGame.NbaGameDate>>
}