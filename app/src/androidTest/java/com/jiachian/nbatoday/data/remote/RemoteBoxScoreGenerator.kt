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
import com.jiachian.nbatoday.GameStatusPrepare
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameDateTime
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.boxscore.data.model.local.PlayerActiveStatus
import com.jiachian.nbatoday.boxscore.data.model.remote.BoxScoreDto
import com.jiachian.nbatoday.game.data.model.local.GameStatus

object RemoteBoxScoreGenerator {
    fun get(gameId: String): BoxScoreDto {
        return when (gameId) {
            FinalGameId -> getFinal()
            else -> getPlaying()
        }
    }

    fun getFinal(): BoxScoreDto {
        return BoxScoreDto(
            game = getFinalGame()
        )
    }

    fun getPlaying(): BoxScoreDto {
        return BoxScoreDto(
            game = getPlayingGame()
        )
    }

    private fun getFinalGame(): BoxScoreDto.RemoteGame {
        return BoxScoreDto.RemoteGame(
            gameId = FinalGameId,
            gameEt = "$FinalGameDateTime-",
            gameStatusText = GameStatusFinal,
            gameStatus = GameStatus.FINAL,
            homeTeam = getHomeTeam(),
            awayTeam = getAwayTeam(),
        )
    }

    private fun getPlayingGame(): BoxScoreDto.RemoteGame {
        return BoxScoreDto.RemoteGame(
            gameId = PlayingGameId,
            gameEt = "$PlayingGameDateTime-",
            gameStatusText = GameStatusPrepare,
            gameStatus = GameStatus.PLAYING,
            homeTeam = getHomeTeam(),
            awayTeam = getAwayTeam(),
        )
    }

    private fun getHomeTeam(): BoxScoreDto.RemoteGame.RemoteTeam {
        return BoxScoreDto.RemoteGame.RemoteTeam(
            teamId = HomeTeamId,
            score = BasicNumber,
            periods = listOf(getPeriod()),
            players = listOf(getHomePlayer()),
            statistics = getTeamStatistics(),
        )
    }

    private fun getAwayTeam(): BoxScoreDto.RemoteGame.RemoteTeam {
        return BoxScoreDto.RemoteGame.RemoteTeam(
            teamId = AwayTeamId,
            score = BasicNumber,
            periods = listOf(getPeriod()),
            players = listOf(getAwayPlayer()),
            statistics = getTeamStatistics(),
        )
    }

    private fun getPeriod(): BoxScoreDto.RemoteGame.RemoteTeam.RemotePeriod {
        return BoxScoreDto.RemoteGame.RemoteTeam.RemotePeriod(
            period = 1,
            score = BasicNumber
        )
    }

    private fun getHomePlayer(): BoxScoreDto.RemoteGame.RemoteTeam.RemotePlayer {
        return BoxScoreDto.RemoteGame.RemoteTeam.RemotePlayer(
            status = PlayerActiveStatus.ACTIVE,
            notPlayingReason = "",
            playerId = HomePlayerId,
            position = BasicPosition,
            starter = "1",
            statistics = getPlayerStatistics(),
            nameAbbr = HomePlayerLastName,
        )
    }

    private fun getAwayPlayer(): BoxScoreDto.RemoteGame.RemoteTeam.RemotePlayer {
        return BoxScoreDto.RemoteGame.RemoteTeam.RemotePlayer(
            status = PlayerActiveStatus.ACTIVE,
            notPlayingReason = "",
            playerId = AwayPlayerId,
            position = BasicPosition,
            starter = "1",
            statistics = getPlayerStatistics(),
            nameAbbr = AwayPlayerLastName,
        )
    }

    private fun getPlayerStatistics(): BoxScoreDto.RemoteGame.RemoteTeam.RemotePlayer.RemoteStatistics {
        return BoxScoreDto.RemoteGame.RemoteTeam.RemotePlayer.RemoteStatistics(
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

    private fun getTeamStatistics(): BoxScoreDto.RemoteGame.RemoteTeam.RemoteStatistics {
        return BoxScoreDto.RemoteGame.RemoteTeam.RemoteStatistics(
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
