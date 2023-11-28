package com.jiachian.nbatoday.datasource.remote.game

import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.models.remote.game.RemoteGame
import com.jiachian.nbatoday.models.remote.game.RemoteSchedule
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore

abstract class GameRemoteSource : RemoteSource() {
    abstract suspend fun getSchedule(): RemoteSchedule?
    abstract suspend fun getScoreboard(leagueId: String, gameDate: String): RemoteGame?
    abstract suspend fun getScoreboard(
        leagueId: String,
        year: Int,
        month: Int,
        day: Int,
        offset: Int
    ): List<RemoteGame>?

    abstract suspend fun getGameBoxScore(gameId: String): RemoteBoxScore?
}
