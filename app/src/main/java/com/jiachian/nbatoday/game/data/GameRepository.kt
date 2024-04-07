package com.jiachian.nbatoday.game.data

import com.jiachian.nbatoday.boxscore.data.model.local.BoxScoreAndGame
import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import kotlinx.coroutines.flow.Flow

/**
 * Manage game-related data.
 */
interface GameRepository {
    /**
     * Inserts the box score for a specified game.
     *
     * @param gameId The ID of the game for which the box score is inserted.
     */
    suspend fun addBoxScore(gameId: String): Response<Unit>

    /**
     * Retrieves a flow of games and associated bets that occurred within a specified time range.
     *
     * @param from The start date-time of the time range.
     * @param to The end date-time of the time range.
     * @return A Flow emitting a list of GameAndBets objects.
     */
    fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>>

    /**
     * Retrieves a flow of box score and game information for a specific game.
     *
     * @param gameId The ID of the game for which to retrieve box score and game information.
     * @return A Flow emitting a BoxScoreAndGame object, or null if no information is available.
     */
    fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?>

    /**
     * Retrieves a flow of games and associated bets.
     *
     * @return A Flow emitting a list of GameAndBets objects.
     */
    fun getGamesAndBets(): Flow<List<GameAndBets>>

    suspend fun getGameAndBets(gameId: String): GameAndBets?

    /**
     * Retrieves a flow of games and associated bets that occurred before a specified date-time for a specific team.
     *
     * @param teamId The ID of the team for which to retrieve games and bets.
     * @param from The date-time before which the games occurred.
     * @return A Flow emitting a list of GameAndBets objects.
     */
    fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>>

    /**
     * Retrieves a flow of games and associated bets that occurred after a specified date-time for a specific team.
     *
     * @param teamId The ID of the team for which to retrieve games and bets.
     * @param from The date-time after which the games occurred.
     * @return A Flow emitting a list of GameAndBets objects.
     */
    fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>>
}
