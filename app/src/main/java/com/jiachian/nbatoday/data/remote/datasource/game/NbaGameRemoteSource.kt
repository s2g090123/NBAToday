package com.jiachian.nbatoday.data.remote.datasource.game

import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.service.GameService

class NbaGameRemoteSource : GameRemoteSource() {

    private val gameService by lazy {
        retrofit.create(GameService::class.java)
    }

    override suspend fun getSchedule(): Schedule? {
        return gameService.getSchedule().body()
    }

    override suspend fun getScoreboard(leagueId: String, gameDate: String): GameScoreboard? {
        return gameService.getScoreboard(leagueId, gameDate).body()
    }

    override suspend fun getScoreboard(
        leagueId: String,
        year: Int,
        month: Int,
        day: Int,
        offset: Int
    ): List<GameScoreboard>? {
        return gameService.getScoreboards(leagueId, year, month, day, offset).body()
    }

    override suspend fun getGameBoxScore(gameId: String): RemoteGameBoxScore? {
        return gameService.getGameBoxScore(gameId).body()
    }
}
