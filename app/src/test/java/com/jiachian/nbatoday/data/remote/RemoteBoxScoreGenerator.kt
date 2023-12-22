package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicMinutes
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.FinalGameDateTime
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.models.local.score.PlayerActiveStatus
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore

object RemoteBoxScoreGenerator {
    fun getFinal(): RemoteBoxScore {
        return RemoteBoxScore(
            game = getFinalGame()
        )
    }

    private fun getFinalGame(): RemoteBoxScore.RemoteGame {
        return RemoteBoxScore.RemoteGame(
            gameId = FinalGameId,
            gameEt = "$FinalGameDateTime-",
            gameStatusText = GameStatusFinal,
            gameStatus = GameStatus.FINAL,
            homeTeam = getHomeTeam(),
            awayTeam = getAwayTeam(),
        )
    }

    private fun getHomeTeam(): RemoteBoxScore.RemoteGame.RemoteTeam {
        return RemoteBoxScore.RemoteGame.RemoteTeam(
            teamId = HomeTeamId,
            score = BasicNumber,
            periods = listOf(getPeriod()),
            players = listOf(getHomePlayer()),
            statistics = getTeamStatistics(),
        )
    }

    private fun getAwayTeam(): RemoteBoxScore.RemoteGame.RemoteTeam {
        return RemoteBoxScore.RemoteGame.RemoteTeam(
            teamId = AwayTeamId,
            score = BasicNumber,
            periods = listOf(getPeriod()),
            players = listOf(getAwayPlayer()),
            statistics = getTeamStatistics(),
        )
    }

    private fun getPeriod(): RemoteBoxScore.RemoteGame.RemoteTeam.RemotePeriod {
        return RemoteBoxScore.RemoteGame.RemoteTeam.RemotePeriod(
            period = 1,
            score = BasicNumber
        )
    }

    private fun getHomePlayer(): RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer {
        return RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer(
            status = PlayerActiveStatus.ACTIVE,
            notPlayingReason = "",
            playerId = HomePlayerId,
            position = BasicPosition,
            starter = "1",
            statistics = getPlayerStatistics(),
            nameAbbr = HomePlayerLastName,
        )
    }

    private fun getAwayPlayer(): RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer {
        return RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer(
            status = PlayerActiveStatus.ACTIVE,
            notPlayingReason = "",
            playerId = AwayPlayerId,
            position = BasicPosition,
            starter = "1",
            statistics = getPlayerStatistics(),
            nameAbbr = AwayPlayerLastName,
        )
    }

    private fun getPlayerStatistics(): RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer.RemoteStatistics {
        return RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer.RemoteStatistics(
            assists = BasicNumber,
            blocks = BasicNumber,
            blocksReceived = BasicNumber,
            fieldGoalsAttempted = BasicNumber,
            fieldGoalsMade = BasicNumber,
            fieldGoalsPercentage = BasicPercentage,
            foulsOffensive = BasicNumber,
            foulsPersonal = BasicNumber,
            foulsTechnical = BasicNumber,
            freeThrowsAttempted = BasicNumber,
            freeThrowsMade = BasicNumber,
            freeThrowsPercentage = BasicPercentage,
            minutes = BasicMinutes,
            plusMinusPoints = BasicNumber.toDouble(),
            points = BasicNumber,
            reboundsOffensive = BasicNumber,
            reboundsDefensive = BasicNumber,
            reboundsTotal = BasicNumber,
            steals = BasicNumber,
            threePointersAttempted = BasicNumber,
            threePointersMade = BasicNumber,
            threePointersPercentage = BasicPercentage,
            turnovers = BasicNumber,
            twoPointersAttempted = BasicNumber,
            twoPointersMade = BasicNumber,
            twoPointersPercentage = BasicPercentage
        )
    }

    private fun getTeamStatistics(): RemoteBoxScore.RemoteGame.RemoteTeam.RemoteStatistics {
        return RemoteBoxScore.RemoteGame.RemoteTeam.RemoteStatistics(
            assists = BasicNumber,
            blocks = BasicNumber,
            fieldGoalsAttempted = BasicNumber,
            fieldGoalsMade = BasicNumber,
            fieldGoalsPercentage = BasicPercentage,
            foulsPersonal = BasicNumber,
            foulsTechnical = BasicNumber,
            freeThrowsAttempted = BasicNumber,
            freeThrowsMade = BasicNumber,
            freeThrowsPercentage = BasicPercentage,
            points = BasicNumber,
            reboundsOffensive = BasicNumber,
            reboundsDefensive = BasicNumber,
            reboundsTotal = BasicNumber,
            steals = BasicNumber,
            threePointersAttempted = BasicNumber,
            threePointersMade = BasicNumber,
            threePointersPercentage = BasicPercentage,
            turnovers = BasicNumber,
            twoPointersAttempted = BasicNumber,
            twoPointersMade = BasicNumber,
            twoPointersPercentage = BasicPercentage,
            benchPoints = BasicNumber,
            pointsFastBreak = BasicNumber,
            pointsFromTurnovers = BasicNumber,
            pointsInThePaint = BasicNumber,
            pointsSecondChance = BasicNumber,
        )
    }
}
