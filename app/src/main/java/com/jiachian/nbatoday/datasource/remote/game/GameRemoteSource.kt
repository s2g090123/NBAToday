package com.jiachian.nbatoday.datasource.remote.game

import com.jiachian.nbatoday.models.remote.game.RemoteGame
import com.jiachian.nbatoday.models.remote.game.RemoteSchedule
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore
import retrofit2.Response

/**
 * The remote data source for game-related operations.
 */
abstract class GameRemoteSource {
    /**
     * Retrieves the schedule of NBA games from the remote data source.
     *
     * @return A [Response] containing a [RemoteSchedule] object.
     */
    abstract suspend fun getSchedule(): Response<RemoteSchedule>

    /**
     * Retrieves information about a specific NBA game from the remote data source.
     *
     * @param leagueId The ID of the NBA league.
     * @param gameDate The date of the game.
     * @return A [Response] containing a [RemoteGame] object.
     */
    abstract suspend fun getGame(leagueId: String, gameDate: String): Response<RemoteGame>

    /**
     * Retrieves a list of NBA games for a specific date range from the remote data source.
     *
     * @param year The year of the date range.
     * @param month The month of the date range.
     * @param day The day of the date range.
     * @param total The total number of games to retrieve.
     * @return A [Response] containing a list of [RemoteGame] objects.
     */
    abstract suspend fun getGames(
        year: Int,
        month: Int,
        day: Int,
        total: Int
    ): Response<List<RemoteGame>>

    /**
     * Retrieves the box score of a specific NBA game from the remote data source.
     *
     * @param gameId The ID of the NBA game.
     * @return A [Response] containing a [RemoteBoxScore] object.
     */
    abstract suspend fun getBoxScore(gameId: String): Response<RemoteBoxScore>
}
