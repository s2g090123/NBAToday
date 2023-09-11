package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.USER_NAME
import com.jiachian.nbatoday.USER_POINTS
import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.data.remote.player.RemoteTeamPlayerStats
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore
import com.jiachian.nbatoday.data.remote.team.RemoteTeamStats
import com.jiachian.nbatoday.data.remote.user.User

class TestRemoteDataSource : RemoteDataSource() {
    override suspend fun getSchedule(): Schedule {
        return RemoteGameFactory.getRemoteSchedule()
    }

    override suspend fun getScoreboard(leagueId: String, gameDate: String): GameScoreboard {
        return RemoteGameFactory.getRemoteGameScoreboard()
    }

    override suspend fun getScoreboard(
        leagueId: String,
        year: Int,
        month: Int,
        day: Int,
        offset: Int
    ): List<GameScoreboard> {
        return listOf(RemoteGameFactory.getRemoteGameScoreboard())
    }

    override suspend fun getGameBoxScore(gameId: String): RemoteGameBoxScore {
        return RemoteGameFactory.getRemoteGameBoxScore()
    }

    override suspend fun getTeamStats(): RemoteTeamStats {
        return RemoteTeamFactory.getRemoteTeamStats()
    }

    override suspend fun getTeamStats(teamId: Int): RemoteTeamStats {
        return RemoteTeamFactory.getRemoteTeamStats()
    }

    override suspend fun getTeamPlayersStats(teamId: Int): RemoteTeamPlayerStats {
        return RemoteTeamFactory.getRemoteTeamPlayerStats()
    }

    override suspend fun getPlayerInfo(playerId: Int): RemotePlayerInfo {
        return RemotePlayerFactory.getRemotePlayerInfo()
    }

    override suspend fun getPlayerCareerStats(playerId: Int): RemotePlayerStats {
        return RemotePlayerFactory.getRemotePlayerStats()
    }

    override suspend fun getPlayerDetail(playerId: Int): RemotePlayerDetail {
        return RemotePlayerFactory.getRemotePlayerDetail()
    }

    override suspend fun login(account: String, password: String): User {
        return User(account, USER_NAME, USER_POINTS, password, "")
    }

    override suspend fun register(account: String, password: String): User {
        return User(account, USER_NAME, USER_POINTS, password, "")
    }

    override suspend fun updatePassword(account: String, password: String, token: String): String {
        return "success"
    }

    override suspend fun updatePoints(account: String, points: Long, token: String): String {
        return "success"
    }
}
