package com.jiachian.nbatoday.datasource.remote.game

import com.jiachian.nbatoday.models.remote.game.RemoteGame
import com.jiachian.nbatoday.models.remote.game.RemoteSchedule
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore
import com.jiachian.nbatoday.service.GameService

class NbaGameRemoteSource : GameRemoteSource() {

    private val gameService by lazy {
        retrofit.create(GameService::class.java)
    }

    override suspend fun getSchedule(): RemoteSchedule? {
        return gameService.getSchedule().body()
    }

    override suspend fun getScoreboard(leagueId: String, gameDate: String): RemoteGame? {
        return gameService.getScoreboard(leagueId, gameDate).body()
    }

    override suspend fun getScoreboard(
        leagueId: String,
        year: Int,
        month: Int,
        day: Int,
        offset: Int
    ): List<RemoteGame>? {
        return gameService.getScoreboards(leagueId, year, month, day, offset).body()
    }

    override suspend fun getGameBoxScore(gameId: String): RemoteBoxScore? {
        return gameService.getGameBoxScore(gameId).body()
    }
}
