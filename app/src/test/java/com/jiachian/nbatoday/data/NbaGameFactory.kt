package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.*
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import java.util.*

object NbaGameFactory {

    fun getFinalGame(): NbaGame {
        return NbaGame(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GAME_CODE,
            gameId = FINAL_GAME_ID,
            gameStatus = GameStatusCode.FINAL,
            gameStatusText = GAME_STATUS_FINAL,
            gameSequence = 1,
            homeTeam = GameTeamFactory.getDefaultHomeTeam(),
            gameDate = Date(BASIC_TIME),
            gameDateTime = Date(BASIC_TIME),
            monthNum = 1,
            pointsLeaders = listOf(
                GameLeaderFactory.getHomePointsLeader(),
                GameLeaderFactory.getAwayPointsLeader()
            ),
            weekNumber = 1,
            gameLeaders = GameLeaderFactory.getGameLeaders(),
            teamLeaders = GameLeaderFactory.getGameLeaders()
        )
    }

    fun getPlayingGame(): NbaGame {
        return NbaGame(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GAME_CODE,
            gameId = PLAYING_GAME_ID,
            gameStatus = GameStatusCode.PLAYING,
            gameStatusText = GAME_STATUS_PREPARE,
            gameSequence = 2,
            homeTeam = GameTeamFactory.getDefaultHomeTeam(),
            gameDate = Date(BASIC_TIME),
            gameDateTime = Date(BASIC_TIME),
            monthNum = 1,
            pointsLeaders = listOf(
                GameLeaderFactory.getHomePointsLeader(),
                GameLeaderFactory.getAwayPointsLeader()
            ),
            weekNumber = 1,
            gameLeaders = GameLeaderFactory.getGameLeaders(),
            teamLeaders = GameLeaderFactory.getGameLeaders()
        )
    }

    fun getComingSoonGame(): NbaGame {
        return NbaGame(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GAME_CODE,
            gameId = COMING_SOON_GAME_ID,
            gameStatus = GameStatusCode.COMING_SOON,
            gameStatusText = GAME_STATUS_PREPARE,
            gameSequence = 3,
            homeTeam = GameTeamFactory.getDefaultHomeTeam(),
            gameDate = Date(BASIC_TIME),
            gameDateTime = Date(BASIC_TIME),
            monthNum = 1,
            pointsLeaders = listOf(
                GameLeaderFactory.getHomePointsLeader(),
                GameLeaderFactory.getAwayPointsLeader()
            ),
            weekNumber = 1,
            gameLeaders = GameLeaderFactory.getGameLeaders(),
            teamLeaders = GameLeaderFactory.getGameLeaders()
        )
    }
}