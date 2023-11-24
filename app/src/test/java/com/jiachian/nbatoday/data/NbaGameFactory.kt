package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.BasicTime
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameCode
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.NextTime
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import java.util.Date

object NbaGameFactory {

    fun getFinalGame(): NbaGame {
        return NbaGame(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GameCode,
            gameId = FinalGameId,
            gameStatus = GameStatusCode.FINAL,
            gameStatusText = GameStatusFinal,
            gameSequence = 1,
            homeTeam = GameTeamFactory.getDefaultHomeTeam(),
            gameDate = Date(BasicTime),
            gameDateTime = Date(BasicTime),
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
            gameCode = GameCode,
            gameId = PlayingGameId,
            gameStatus = GameStatusCode.PLAYING,
            gameStatusText = GameStatusPrepare,
            gameSequence = 2,
            homeTeam = GameTeamFactory.getDefaultHomeTeam(),
            gameDate = Date(BasicTime),
            gameDateTime = Date(BasicTime),
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
            gameCode = GameCode,
            gameId = ComingSoonGameId,
            gameStatus = GameStatusCode.COMING_SOON,
            gameStatusText = GameStatusPrepare,
            gameSequence = 3,
            homeTeam = GameTeamFactory.getDefaultHomeTeam(),
            gameDate = Date(NextTime),
            gameDateTime = Date(NextTime),
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
