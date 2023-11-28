package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.BasicTime
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameCode
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.NextTime
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameStatus
import java.util.Date

object NbaGameFactory {

    fun getFinalGame(): Game {
        return Game(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GameCode,
            gameId = FinalGameId,
            gameStatus = GameStatus.FINAL,
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

    fun getPlayingGame(): Game {
        return Game(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GameCode,
            gameId = PlayingGameId,
            gameStatus = GameStatus.PLAYING,
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

    fun getComingSoonGame(): Game {
        return Game(
            leagueId = "00",
            awayTeam = GameTeamFactory.getDefaultAwayTeam(),
            day = "SUN",
            gameCode = GameCode,
            gameId = ComingSoonGameId,
            gameStatus = GameStatus.COMING_SOON,
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
