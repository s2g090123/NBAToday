package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicTime
import com.jiachian.nbatoday.ComingSoonGameId
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameStatus
import java.util.Date

object GameGenerator {
    fun getFinal(): Game {
        return Game(
            homeTeamId = HomeTeamId,
            awayTeamId = AwayTeamId,
            awayTeam = GameTeamGenerator.getAway(),
            gameId = FinalGameId,
            gameStatus = GameStatus.FINAL,
            gameStatusText = GameStatusFinal,
            homeTeam = GameTeamGenerator.getHome(),
            gameDate = Date(BasicTime),
            gameDateTime = Date(BasicTime),
            pointsLeaders = listOf(
                getHomePointsLeader(),
                getAwayPointsLeader()
            ),
            gameLeaders = GameLeadersGenerator.get(),
            teamLeaders = GameLeadersGenerator.get()
        )
    }

    fun getPlaying(): Game {
        return Game(
            homeTeamId = HomeTeamId,
            awayTeamId = AwayTeamId,
            awayTeam = GameTeamGenerator.getAway(),
            gameId = PlayingGameId,
            gameStatus = GameStatus.PLAYING,
            gameStatusText = GameStatusPrepare,
            homeTeam = GameTeamGenerator.getHome(),
            gameDate = Date(BasicTime),
            gameDateTime = Date(BasicTime),
            pointsLeaders = listOf(
                getHomePointsLeader(),
                getAwayPointsLeader()
            ),
            gameLeaders = GameLeadersGenerator.get(),
            teamLeaders = GameLeadersGenerator.get()
        )
    }

    fun getComingSoon(): Game {
        return Game(
            homeTeamId = HomeTeamId,
            awayTeamId = AwayTeamId,
            awayTeam = GameTeamGenerator.getAway(),
            gameId = ComingSoonGameId,
            gameStatus = GameStatus.COMING_SOON,
            gameStatusText = GameStatusPrepare,
            homeTeam = GameTeamGenerator.getHome(),
            gameDate = Date(BasicTime),
            gameDateTime = Date(BasicTime),
            pointsLeaders = listOf(
                getHomePointsLeader(),
                getAwayPointsLeader()
            ),
            gameLeaders = GameLeadersGenerator.get(),
            teamLeaders = GameLeadersGenerator.get()
        )
    }

    private fun getHomePointsLeader(): Game.PointsLeader {
        return Game.PointsLeader(
            playerId = HomePlayerId,
            points = BasicNumber.toDouble(),
            teamId = HomeTeamId,
        )
    }

    private fun getAwayPointsLeader(): Game.PointsLeader {
        return Game.PointsLeader(
            playerId = AwayPlayerId,
            points = BasicNumber.toDouble(),
            teamId = AwayTeamId,
        )
    }
}
