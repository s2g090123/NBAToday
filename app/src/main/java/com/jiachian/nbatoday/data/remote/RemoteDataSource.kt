package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.data.remote.user.User

abstract class RemoteDataSource {
    abstract suspend fun getSchedule(): Schedule?
    abstract suspend fun getScoreboard(leagueId: String, gameDate: String): GameScoreboard?
    abstract suspend fun getScoreboard(
        leagueId: String,
        year: Int,
        month: Int,
        day: Int,
        offset: Int
    ): List<GameScoreboard>?

    abstract suspend fun getGameBoxScore(gameId: String): RemoteGameBoxScore?
    abstract suspend fun getTeamStats(): RemoteTeamStats?
    abstract suspend fun getTeamStats(teamId: Int): RemoteTeamStats?
    abstract suspend fun getTeamPlayersStats(teamId: Int): RemoteTeamPlayerStats?
    abstract suspend fun getPlayerInfo(playerId: Int): RemotePlayerInfo?
    abstract suspend fun getPlayerCareerStats(playerId: Int): RemotePlayerStats?
    abstract suspend fun getPlayerDetail(playerId: Int): RemotePlayerDetail?

    /** User */
    abstract suspend fun login(account: String, password: String): User?
    abstract suspend fun register(account: String, password: String): User?
    abstract suspend fun updatePassword(account: String, password: String, token: String): String?
    abstract suspend fun updatePoints(account: String, points: Long, token: String): String?
}
