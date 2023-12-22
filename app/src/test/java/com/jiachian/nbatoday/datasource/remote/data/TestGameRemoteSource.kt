package com.jiachian.nbatoday.datasource.remote.data

import com.jiachian.nbatoday.data.remote.RemoteBoxScoreGenerator
import com.jiachian.nbatoday.data.remote.RemoteGameGenerator
import com.jiachian.nbatoday.data.remote.RemoteScheduleGenerator
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.models.remote.game.RemoteGame
import com.jiachian.nbatoday.models.remote.game.RemoteSchedule
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore
import retrofit2.Response

class TestGameRemoteSource : GameRemoteSource() {
    override suspend fun getSchedule(): Response<RemoteSchedule> {
        return Response.success(RemoteScheduleGenerator.get())
    }

    override suspend fun getGame(leagueId: String, gameDate: String): Response<RemoteGame> {
        return Response.success(RemoteGameGenerator.get())
    }

    override suspend fun getGames(year: Int, month: Int, day: Int, total: Int): Response<List<RemoteGame>> {
        return Response.success(listOf(RemoteGameGenerator.get()))
    }

    override suspend fun getBoxScore(gameId: String): Response<RemoteBoxScore> {
        return Response.success(RemoteBoxScoreGenerator.getFinal())
    }
}
