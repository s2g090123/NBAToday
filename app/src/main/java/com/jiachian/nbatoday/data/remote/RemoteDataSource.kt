package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule

abstract class RemoteDataSource {
    abstract suspend fun getSchedule(): Schedule?
    abstract suspend fun getScoreboard(leagueId: String, gameDate: String): GameScoreboard?
}