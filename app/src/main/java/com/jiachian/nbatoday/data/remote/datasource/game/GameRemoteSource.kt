package com.jiachian.nbatoday.data.remote.datasource.game

import com.jiachian.nbatoday.data.remote.datasource.RemoteSource
import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore

abstract class GameRemoteSource : RemoteSource() {
    abstract suspend fun getSchedule(): Schedule?
    abstract suspend fun getScoreboard(leagueId: String, gameDate: String): GameScoreboard?
    abstract suspend fun getScoreboard(leagueId: String, year: Int, month: Int, day: Int, offset: Int): List<GameScoreboard>?
    abstract suspend fun getGameBoxScore(gameId: String): RemoteGameBoxScore?
}
