package com.jiachian.nbatoday.datasource.remote.game

import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.models.remote.game.RemoteGame
import com.jiachian.nbatoday.models.remote.game.RemoteSchedule
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore
import retrofit2.Response

abstract class GameRemoteSource : RemoteSource() {
    abstract suspend fun getSchedule(): Response<RemoteSchedule>
    abstract suspend fun getGame(leagueId: String, gameDate: String): Response<RemoteGame>
    abstract suspend fun getGames(
        year: Int,
        month: Int,
        day: Int,
        total: Int
    ): Response<List<RemoteGame>>

    abstract suspend fun getBoxScore(gameId: String): Response<RemoteBoxScore>
}
