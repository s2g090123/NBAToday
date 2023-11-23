package com.jiachian.nbatoday.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM nba_game")
    fun getGamesAndBets(): Flow<List<NbaGameAndBet>>

    @Query("SELECT * FROM nba_game WHERE game_date <= :from AND (home_team_id == :teamId OR away_team_id == :teamId)")
    fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>

    @Query("SELECT * FROM nba_game WHERE game_date > :from AND (home_team_id == :teamId OR away_team_id == :teamId)")
    fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>

    @Query("SELECT * FROM nba_game WHERE game_date >= :from AND game_date <= :to")
    fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>>

    @Query("SELECT EXISTS (SELECT 1 FROM nba_game)")
    fun exitsGames(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<NbaGame>)

    @Update(entity = NbaGame::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGames(games: List<GameUpdateData>)
}
