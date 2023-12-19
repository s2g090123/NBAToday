package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.BasicMinutes
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameDate
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.PlayerActiveStatus

object BoxScoreGenerator {
    fun getFinal(): BoxScore {
        return BoxScore(
            gameId = FinalGameId,
            gameDate = GameDate,
            gameStatusText = GameStatusFinal,
            gameStatus = GameStatus.FINAL,
            homeTeam = getHomeBoxScoreTeam(),
            awayTeam = getAwayBoxScoreTeam(),
        )
    }

    fun getPlaying(): BoxScore {
        return BoxScore(
            gameId = PlayingGameId,
            gameDate = GameDate,
            gameStatusText = GameStatusPrepare,
            gameStatus = GameStatus.PLAYING,
            homeTeam = getHomeBoxScoreTeam(),
            awayTeam = getAwayBoxScoreTeam(),
        )
    }

    private fun getHomeBoxScoreTeam(): BoxScore.BoxScoreTeam {
        return BoxScore.BoxScoreTeam(
            team = NBATeamGenerator.getHome(),
            score = BasicNumber,
            periods = getPeriods(),
            players = listOf(getHomePlayer()),
            statistics = getTeamStats()
        )
    }

    private fun getAwayBoxScoreTeam(): BoxScore.BoxScoreTeam {
        return BoxScore.BoxScoreTeam(
            team = NBATeamGenerator.getAway(),
            score = BasicNumber,
            periods = getPeriods(),
            players = listOf(getAwayPlayer()),
            statistics = getTeamStats()
        )
    }

    private fun getPeriods(): List<BoxScore.BoxScoreTeam.Period> {
        return (1..4).map {
            BoxScore.BoxScoreTeam.Period(
                periodLabel = "1st",
                score = BasicNumber
            )
        }
    }

    private fun getHomePlayer(): BoxScore.BoxScoreTeam.Player {
        return BoxScore.BoxScoreTeam.Player(
            status = PlayerActiveStatus.ACTIVE,
            notPlayingReason = "",
            playerId = HomePlayerId,
            position = BasicPosition,
            starter = true,
            statistics = getPlayerStats(),
            nameAbbr = HomePlayerLastName,
        )
    }

    private fun getAwayPlayer(): BoxScore.BoxScoreTeam.Player {
        return BoxScore.BoxScoreTeam.Player(
            status = PlayerActiveStatus.ACTIVE,
            notPlayingReason = "",
            playerId = AwayPlayerId,
            position = BasicPosition,
            starter = true,
            statistics = getPlayerStats(),
            nameAbbr = AwayPlayerLastName,
        )
    }

    private fun getPlayerStats(): BoxScore.BoxScoreTeam.Player.Statistics {
        return BoxScore.BoxScoreTeam.Player.Statistics(
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
            plusMinusPoints = BasicNumber,
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
        )
    }

    private fun getTeamStats(): BoxScore.BoxScoreTeam.Statistics {
        return BoxScore.BoxScoreTeam.Statistics(
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
            pointsFastBreak = BasicNumber,
            pointsFromTurnovers = BasicNumber,
            pointsInThePaint = BasicNumber,
            pointsSecondChance = BasicNumber,
            benchPoints = BasicNumber
        )
    }
}
