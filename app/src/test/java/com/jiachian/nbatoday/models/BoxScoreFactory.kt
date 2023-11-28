package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.AwayPlayerFirstName
import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.AwayTeam
import com.jiachian.nbatoday.BasicMinutes
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameCode
import com.jiachian.nbatoday.GameDate
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomePlayerFirstName
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeam
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.PlayerActiveStatus

object BoxScoreFactory {

    fun getFinalGameBoxScore(): BoxScore {
        return BoxScore(
            gameId = FinalGameId,
            gameDate = GameDate,
            gameCode = GameCode,
            gameStatusText = GameStatusFinal,
            gameStatus = GameStatus.FINAL,
            homeTeam = getHomeTeamScore(),
            awayTeam = getAwayTeamScore()
        )
    }

    fun getPlayingGameBoxScore(): BoxScore {
        return BoxScore(
            gameId = PlayingGameId,
            gameDate = GameDate,
            gameCode = GameCode,
            gameStatusText = GameStatusPrepare,
            gameStatus = GameStatus.PLAYING,
            homeTeam = getHomeTeamScore(),
            awayTeam = getAwayTeamScore()
        )
    }

    private fun getHomeTeamScore(): BoxScore.BoxScoreTeam {
        return BoxScore.BoxScoreTeam(
            team = HomeTeam,
            score = BasicNumber,
            inBonus = true,
            timeoutsRemaining = BasicNumber,
            periods = createPeriods(),
            players = createHomePlayers(),
            statistics = createTeamStatics()
        )
    }

    private fun getAwayTeamScore(): BoxScore.BoxScoreTeam {
        return BoxScore.BoxScoreTeam(
            team = AwayTeam,
            score = BasicNumber,
            inBonus = true,
            timeoutsRemaining = BasicNumber,
            periods = createPeriods(),
            players = createAwayPlayers(),
            statistics = createTeamStatics()
        )
    }

    private fun createPeriods(): List<BoxScore.BoxScoreTeam.Period> {
        return (1..4).map {
            BoxScore.BoxScoreTeam.Period(
                period = it,
                periodLabel = "1st",
                score = BasicNumber
            )
        }
    }

    private fun createHomePlayers(): List<BoxScore.BoxScoreTeam.Player> {
        return listOf(
            BoxScore.BoxScoreTeam.Player(
                status = PlayerActiveStatus.ACTIVE,
                notPlayingReason = "",
                order = 1,
                playerId = HomePlayerId,
                jerseyNum = BasicNumber.toString(),
                position = BasicPosition,
                starter = true,
                onCourt = true,
                played = true,
                statistics = createPlayerStatics(),
                name = HomePlayerFullName,
                nameAbbr = HomePlayerLastName,
                firstName = HomePlayerFirstName,
                familyName = HomePlayerLastName
            )
        )
    }

    private fun createAwayPlayers(): List<BoxScore.BoxScoreTeam.Player> {
        return listOf(
            BoxScore.BoxScoreTeam.Player(
                status = PlayerActiveStatus.ACTIVE,
                notPlayingReason = "",
                order = 1,
                playerId = AwayPlayerId,
                jerseyNum = BasicNumber.toString(),
                position = BasicPosition,
                starter = true,
                onCourt = true,
                played = true,
                statistics = createPlayerStatics(),
                name = AwayPlayerFullName,
                nameAbbr = AwayPlayerLastName,
                firstName = AwayPlayerFirstName,
                familyName = AwayPlayerLastName
            )
        )
    }

    private fun createTeamStatics(): BoxScore.BoxScoreTeam.Statistics {
        return BoxScore.BoxScoreTeam.Statistics(
            assists = BasicNumber,
            blocks = BasicNumber,
            blocksReceived = BasicNumber,
            fieldGoalsAttempted = BasicNumber,
            fieldGoalsMade = BasicNumber,
            fieldGoalsPercentage = BasicPercentage,
            foulsOffensive = BasicNumber,
            foulsDrawn = BasicNumber,
            foulsPersonal = BasicNumber,
            foulsTeam = BasicNumber,
            foulsTechnical = BasicNumber,
            freeThrowsAttempted = BasicNumber,
            freeThrowsMade = BasicNumber,
            freeThrowsPercentage = BasicPercentage,
            points = BasicNumber,
            reboundsOffensive = BasicNumber,
            reboundsDefensive = BasicNumber,
            reboundsPersonal = BasicNumber,
            reboundsTotal = BasicNumber,
            steals = BasicNumber,
            threePointersAttempted = BasicNumber,
            threePointersMade = BasicNumber,
            threePointersPercentage = BasicPercentage,
            turnovers = BasicNumber,
            turnoversTeam = BasicNumber,
            turnoversTotal = BasicNumber,
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

    private fun createPlayerStatics(): BoxScore.BoxScoreTeam.Player.Statistics {
        return BoxScore.BoxScoreTeam.Player.Statistics(
            assists = BasicNumber,
            blocks = BasicNumber,
            blocksReceived = BasicNumber,
            fieldGoalsAttempted = BasicNumber,
            fieldGoalsMade = BasicNumber,
            fieldGoalsPercentage = BasicPercentage,
            foulsOffensive = BasicNumber,
            foulsDrawn = BasicNumber,
            foulsPersonal = BasicNumber,
            foulsTechnical = BasicNumber,
            freeThrowsAttempted = BasicNumber,
            freeThrowsMade = BasicNumber,
            freeThrowsPercentage = BasicPercentage,
            minus = BasicNumber,
            minutes = BasicMinutes,
            plus = BasicNumber,
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
}
