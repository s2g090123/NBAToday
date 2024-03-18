package com.jiachian.nbatoday.repository.game

import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.BoxScoreAndGame
import com.jiachian.nbatoday.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

/**
 * Manage game-related data.
 */
abstract class GameRepository : BaseRepository() {
    /**
     * Inserts the box score for a specified game.
     *
     * @param gameId The ID of the game for which the box score is inserted.
     */
    abstract suspend fun addBoxScore(gameId: String): BoxScore?

    /**
     * Retrieves a flow of games and associated bets that occurred within a specified time range.
     *
     * @param from The start date-time of the time range.
     * @param to The end date-time of the time range.
     * @return A Flow emitting a list of GameAndBets objects.
     */
    abstract fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<GameAndBets>>

    /**
     * Retrieves a flow of box score and game information for a specific game.
     *
     * @param gameId The ID of the game for which to retrieve box score and game information.
     * @return A Flow emitting a BoxScoreAndGame object, or null if no information is available.
     */
    abstract fun getBoxScoreAndGame(gameId: String): Flow<BoxScoreAndGame?>

    /**
     * Retrieves a flow of games and associated bets.
     *
     * @return A Flow emitting a list of GameAndBets objects.
     */
    abstract fun getGamesAndBets(): Flow<List<GameAndBets>>

    abstract suspend fun getGameAndBet(gameId: String): GameAndBets

    /**
     * Retrieves a flow of games and associated bets that occurred before a specified date-time for a specific team.
     *
     * @param teamId The ID of the team for which to retrieve games and bets.
     * @param from The date-time before which the games occurred.
     * @return A Flow emitting a list of GameAndBets objects.
     */
    abstract fun getGamesAndBetsBefore(teamId: Int, from: Long): Flow<List<GameAndBets>>

    /**
     * Retrieves a flow of games and associated bets that occurred after a specified date-time for a specific team.
     *
     * @param teamId The ID of the team for which to retrieve games and bets.
     * @param from The date-time after which the games occurred.
     * @return A Flow emitting a list of GameAndBets objects.
     */
    abstract fun getGamesAndBetsAfter(teamId: Int, from: Long): Flow<List<GameAndBets>>
}
