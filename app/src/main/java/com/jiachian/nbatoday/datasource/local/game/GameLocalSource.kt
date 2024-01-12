package com.jiachian.nbatoday.datasource.local.game

import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import java.util.Date
import kotlinx.coroutines.flow.Flow

/**
 * The local data source for game-related operations.
 */
abstract class GameLocalSource {
    /**
     * Retrieves a flow of [GameAndBets] objects representing games and associated bets.
     *
     * @return A [Flow] emitting a list of [GameAndBets] objects.
     */
    abstract fun getGamesAndBets(): Flow<List<GameAndBets>>

    /**
     * Retrieves a flow of [GameAndBets] objects representing games and associated bets within a specified date range.
     *
     * @param from The start of the date range (in milliseconds since epoch).
     * @param to The end of the date range (in milliseconds since epoch).
     * @return A [Flow] emitting a list of [GameAndBets] objects.
     */
    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>>

    /**
     * Retrieves a flow of [GameAndBets] objects representing games and associated bets before a specific date for a given team.
     *
     * @param teamId The ID of the team.
     * @param from The reference date (in milliseconds since epoch).
     * @return A [Flow] emitting a list of [GameAndBets] objects.
     */
    abstract fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>>

    /**
     * Retrieves a flow of [GameAndBets] objects representing games and associated bets after a specific date for a given team.
     *
     * @param teamId The ID of the team.
     * @param from The reference date (in milliseconds since epoch).
     * @return A [Flow] emitting a list of [GameAndBets] objects.
     */
    abstract fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>>

    /**
     * Retrieves a flow of [Date] representing the last game date and time in the local data source.
     *
     * @return A [Flow] emitting a nullable [Date] object.
     */
    abstract fun getLastGameDateTime(): Flow<Date?>

    /**
     * Retrieves a flow of [Date] representing the first game date and time in the local data source.
     *
     * @return A [Flow] emitting a nullable [Date] object.
     */
    abstract fun getFirstGameDateTime(): Flow<Date?>

    /**
     * Inserts a list of [Game] objects into the local data source.
     *
     * @param games The list of [Game] objects to be inserted.
     */
    abstract suspend fun insertGames(games: List<Game>)

    /**
     * Updates a list of [Game] objects in the local data source.
     *
     * @param games The list of [GameUpdateData] objects containing the updated game information.
     */
    abstract suspend fun updateGames(games: List<GameUpdateData>)

    /**
     * Updates the scores of a list of [Game] objects in the local data source.
     *
     * @param games The list of [GameScoreUpdateData] objects containing the updated game scores.
     */
    abstract suspend fun updateGameScores(games: List<GameScoreUpdateData>)

    /**
     * Checks if there are existing games in the local data source.
     *
     * @return `true` if there are existing games, `false` otherwise.
     */
    abstract suspend fun gameExists(): Boolean
}
