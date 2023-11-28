package com.jiachian.nbatoday.models.remote

import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.models.remote.game.RemoteGame
import com.jiachian.nbatoday.models.remote.game.RemoteSchedule
import com.jiachian.nbatoday.models.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.models.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.models.remote.player.RemotePlayerStats
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayerStats
import com.jiachian.nbatoday.models.remote.team.RemoteTeamStats

class TestRemoteDataSource : RemoteDataSource() {
    override suspend fun getSchedule(): RemoteSchedule {
        return RemoteGameFactory.getRemoteSchedule()
    }

    override suspend fun getScoreboard(leagueId: String, gameDate: String): RemoteGame {
        return RemoteGameFactory.getRemoteGameScoreboard()
    }

    override suspend fun getScoreboard(
        leagueId: String,
        year: Int,
        month: Int,
        day: Int,
        offset: Int
    ): List<RemoteGame> {
        return listOf(RemoteGameFactory.getRemoteGameScoreboard())
    }

    override suspend fun getGameBoxScore(gameId: String): RemoteBoxScore {
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
        return User(account, UserName, UserPoints, password, "")
    }

    override suspend fun register(account: String, password: String): User {
        return User(account, UserName, UserPoints, password, "")
    }

    override suspend fun updatePassword(account: String, password: String, token: String): String {
        return "success"
    }

    override suspend fun updatePoints(account: String, points: Long, token: String): String {
        return "success"
    }
}
