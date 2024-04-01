package com.jiachian.nbatoday.game.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import com.jiachian.nbatoday.game.data.model.local.GameScoreUpdateData
import com.jiachian.nbatoday.game.data.model.local.GameUpdateData
import kotlinx.coroutines.flow.Flow

/**
 * Handle operations related to the Game entity in the database.
 */
@Dao
interface GameDao {
    /**
     * Retrieves a flow of all games and associated bets from the database.
     *
     * @return Flow emitting a list of [GameAndBets].
     */
    @Query("SELECT * FROM game ORDER BY game_date_time")
    fun getGamesAndBets(): Flow<List<GameAndBets>>

    /**
     * Retrieves a flow of games and associated bets before a specific date for a given team from the database.
     *
     * @param teamId The ID of the team for which to retrieve games.
     * @param from The starting date for filtering games.
     * @return Flow emitting a list of [GameAndBets].
     */
    @Query("SELECT * FROM game WHERE game_date <= :from AND (home_team_id == :teamId OR away_team_id == :teamId)")
    fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>>

    /**
     * Retrieves a flow of games and associated bets after a specific date for a given team from the database.
     *
     * @param teamId The ID of the team for which to retrieve games.
     * @param from The starting date for filtering games.
     * @return Flow emitting a list of [GameAndBets].
     */
    @Query("SELECT * FROM game WHERE game_date > :from AND (home_team_id == :teamId OR away_team_id == :teamId)")
    fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>>

    @Query("SELECT * FROM game WHERE game_id == :gameId")
    suspend fun getGameAndBets(gameId: String): GameAndBets

    /**
     * Retrieves a flow of games and associated bets within a specific date range from the database.
     *
     * @param from The starting date for filtering games.
     * @param to The ending date for filtering games.
     * @return Flow emitting a list of [GameAndBets].
     */
    @Query("SELECT * FROM game WHERE game_date >= :from AND game_date <= :to")
    fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>>

    /**
     * Checks if there are any games in the database.
     *
     * @return True if games exist; false otherwise.
     */
    @Query("SELECT EXISTS (SELECT 1 FROM game)")
    suspend fun gameExists(): Boolean

    /**
     * Inserts a list of games into the database, replacing any existing entries on conflict.
     *
     * @param games The list of [Game] objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGames(games: List<Game>)

    /**
     * Updates a list of games in the database.
     *
     * @param games The list of [GameUpdateData] objects containing updated game information.
     */
    @Update(entity = Game::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGames(games: List<GameUpdateData>)

    /**
     * Updates the scores of a list of games in the database.
     *
     * @param games The list of [GameScoreUpdateData] objects containing updated game scores.
     */
    @Update(entity = Game::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGameScores(games: List<GameScoreUpdateData>)
}
