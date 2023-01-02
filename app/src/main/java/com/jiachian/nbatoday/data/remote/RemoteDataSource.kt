package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats

abstract class RemoteDataSource {
    abstract suspend fun getSchedule(): Schedule?
    abstract suspend fun getScoreboard(leagueId: String, gameDate: String): GameScoreboard?
    abstract suspend fun getGameBoxScore(gameId: String): RemoteGameBoxScore?
    abstract suspend fun getTeamStats(): RemoteTeamStats?
    abstract suspend fun getTeamStats(teamId: Int): RemoteTeamStats?
    abstract suspend fun getTeamPlayersStats(teamId: Int): RemoteTeamPlayerStats?
}