package com.jiachian.nbatoday.data.remote.datasource.game

import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.service.ServiceProvider

class NbaGameRemoteSource : GameRemoteSource() {

    private val nbaService = ServiceProvider.nbaService

    override suspend fun getSchedule(): Schedule? {
        return nbaService.getSchedule().body()
    }

    override suspend fun getScoreboard(leagueId: String, gameDate: String): GameScoreboard? {
        return nbaService.getScoreboard(leagueId, gameDate).body()
    }

    override suspend fun getScoreboard(leagueId: String, year: Int, month: Int, day: Int, offset: Int): List<GameScoreboard>? {
        return nbaService.getScoreboards(leagueId, year, month, day, offset).body()
    }

    override suspend fun getGameBoxScore(gameId: String): RemoteGameBoxScore? {
        return nbaService.getGameBoxScore(gameId).body()
    }
}
