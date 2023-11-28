package com.jiachian.nbatoday.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import com.jiachian.nbatoday.models.local.game.NbaGameAndBet
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getGamesAndBets(): Flow<List<NbaGameAndBet>>

    @Query("SELECT * FROM game WHERE game_date <= :from AND (home_team_id == :teamId OR away_team_id == :teamId)")
    fun getGamesAndBetsBeforeByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>

    @Query("SELECT * FROM game WHERE game_date > :from AND (home_team_id == :teamId OR away_team_id == :teamId)")
    fun getGamesAndBetsAfterByTeam(teamId: Int, from: Long): Flow<List<NbaGameAndBet>>

    @Query("SELECT * FROM game WHERE game_date >= :from AND game_date <= :to")
    fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>>

    @Query("SELECT EXISTS (SELECT 1 FROM game)")
    fun exitsGame(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<Game>)

    @Update(entity = Game::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGames(games: List<GameUpdateData>)

    @Update(entity = Game::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGameScores(games: List<GameScoreUpdateData>)
}
