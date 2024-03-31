package com.jiachian.nbatoday.datasource.remote.data

import com.jiachian.nbatoday.boxscore.data.model.remote.BoxScoreDto
import com.jiachian.nbatoday.data.remote.RemoteBoxScoreGenerator
import com.jiachian.nbatoday.data.remote.RemoteGameGenerator
import com.jiachian.nbatoday.data.remote.RemoteScheduleGenerator
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.game.data.model.remote.GameDto
import com.jiachian.nbatoday.game.data.model.remote.ScheduleDto
import retrofit2.Response

class TestGameRemoteSource : GameRemoteSource() {
    override suspend fun getSchedule(): Response<ScheduleDto> {
        return Response.success(RemoteScheduleGenerator.get())
    }

    override suspend fun getGame(leagueId: String, gameDate: String): Response<GameDto> {
        return Response.success(RemoteGameGenerator.get())
    }

    override suspend fun getGames(year: Int, month: Int, day: Int, total: Int): Response<List<GameDto>> {
        return Response.success(listOf(RemoteGameGenerator.get()))
    }

    override suspend fun getBoxScore(gameId: String): Response<BoxScoreDto> {
        return Response.success(RemoteBoxScoreGenerator.get(gameId))
    }
}
