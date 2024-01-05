package com.jiachian.nbatoday.models.remote.score.extensions

import com.jiachian.nbatoday.FirstPeriod
import com.jiachian.nbatoday.ForthPeriod
import com.jiachian.nbatoday.SecondPeriod
import com.jiachian.nbatoday.ThirdPeriod
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.PlayerActiveStatus
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero

private const val TotalPeriod = 4

private const val PercentageMultiplier = 1000
private const val PercentageDivider = 10.0

fun RemoteBoxScore.RemoteGame.toBoxScore(): BoxScore? {
    if (gameId == null || homeTeam == null || awayTeam == null) return null
    return BoxScore(
        gameId = gameId,
        gameDate = getFormattedGameDate(),
        gameStatusText = gameStatusText.getOrNA(),
        gameStatus = gameStatus ?: GameStatus.COMING_SOON,
        homeTeam = homeTeam.toBoxScoreTeam(),
        awayTeam = awayTeam.toBoxScoreTeam()
    )
}

private fun RemoteBoxScore.RemoteGame.RemoteTeam.RemotePeriod.toPeriod(): BoxScore.BoxScoreTeam.Period {
    val period = period ?: 0
    val periodLabel = when {
        period <= 0 -> ""
        period == FirstPeriod -> "1st"
        period == SecondPeriod -> "2nd"
        period == ThirdPeriod -> "3rd"
        period == ForthPeriod -> "4th"
        else -> "OT${period - TotalPeriod}"
    }
    return BoxScore.BoxScoreTeam.Period(
        periodLabel = periodLabel,
        score = score.getOrZero()
    )
}

private fun RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer.RemoteStatistics.toStatistics():
    BoxScore.BoxScoreTeam.Player.Statistics {
    return BoxScore.BoxScoreTeam.Player.Statistics(
        assists = assists.getOrZero(),
        blocks = blocks.getOrZero(),
        blocksReceived = blocksReceived.getOrZero(),
        fieldGoalsAttempted = fieldGoalsAttempted.getOrZero(),
        fieldGoalsMade = fieldGoalsMade.getOrZero(),
        fieldGoalsPercentage = fieldGoalsPercentage?.toPercentage().getOrZero().decimalFormat(),
        foulsOffensive = foulsOffensive.getOrZero(),
        foulsPersonal = foulsPersonal.getOrZero(),
        foulsTechnical = foulsTechnical.getOrZero(),
        freeThrowsAttempted = freeThrowsAttempted.getOrZero(),
        freeThrowsMade = freeThrowsMade.getOrZero(),
        freeThrowsPercentage = freeThrowsPercentage?.toPercentage().getOrZero().decimalFormat(),
        minutes = getFormattedMinutes(),
        plusMinusPoints = plusMinusPoints?.toInt().getOrZero(),
        points = points.getOrZero(),
        reboundsDefensive = reboundsDefensive.getOrZero(),
        reboundsOffensive = reboundsOffensive.getOrZero(),
        reboundsTotal = reboundsTotal.getOrZero(),
        steals = steals.getOrZero(),
        threePointersAttempted = threePointersAttempted.getOrZero(),
        threePointersMade = threePointersMade.getOrZero(),
        threePointersPercentage = threePointersPercentage?.toPercentage().getOrZero().decimalFormat(),
        turnovers = turnovers.getOrZero(),
        twoPointersAttempted = twoPointersAttempted.getOrZero(),
        twoPointersMade = twoPointersMade.getOrZero(),
        twoPointersPercentage = twoPointersPercentage?.toPercentage().getOrZero().decimalFormat()
    )
}

private fun RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer.toPlayer(): BoxScore.BoxScoreTeam.Player? {
    if (statistics == null || playerId == null) return null
    return BoxScore.BoxScoreTeam.Player(
        status = status ?: PlayerActiveStatus.INACTIVE,
        notPlayingReason = getFormattedNotPlayingReason(),
        playerId = playerId,
        position = position.getOrNA(),
        starter = isStarter(),
        statistics = statistics.toStatistics(),
        nameAbbr = nameAbbr.getOrNA(),
    )
}

private fun RemoteBoxScore.RemoteGame.RemoteTeam.RemoteStatistics.toStatistics(): BoxScore.BoxScoreTeam.Statistics {
    return BoxScore.BoxScoreTeam.Statistics(
        assists = assists.getOrZero(),
        blocks = blocks.getOrZero(),
        fieldGoalsAttempted = fieldGoalsAttempted.getOrZero(),
        fieldGoalsMade = fieldGoalsMade.getOrZero(),
        fieldGoalsPercentage = fieldGoalsPercentage?.toPercentage().getOrZero().decimalFormat(),
        foulsPersonal = foulsPersonal.getOrZero(),
        foulsTechnical = foulsTechnical.getOrZero(),
        freeThrowsAttempted = freeThrowsAttempted.getOrZero(),
        freeThrowsMade = freeThrowsMade.getOrZero(),
        freeThrowsPercentage = freeThrowsPercentage?.toPercentage().getOrZero().decimalFormat(),
        points = points.getOrZero(),
        reboundsDefensive = reboundsDefensive.getOrZero(),
        reboundsOffensive = reboundsOffensive.getOrZero(),
        reboundsTotal = reboundsTotal.getOrZero(),
        steals = steals.getOrZero(),
        threePointersAttempted = threePointersAttempted.getOrZero(),
        threePointersMade = threePointersMade.getOrZero(),
        threePointersPercentage = threePointersPercentage?.toPercentage().getOrZero().decimalFormat(),
        turnovers = turnovers.getOrZero(),
        twoPointersAttempted = twoPointersAttempted.getOrZero(),
        twoPointersMade = twoPointersMade.getOrZero(),
        twoPointersPercentage = twoPointersPercentage?.toPercentage().getOrZero().decimalFormat(),
        pointsFastBreak = pointsFastBreak.getOrZero(),
        pointsFromTurnovers = pointsFromTurnovers.getOrZero(),
        pointsInThePaint = pointsInThePaint.getOrZero(),
        pointsSecondChance = pointsSecondChance.getOrZero(),
        benchPoints = benchPoints.getOrZero()
    )
}

private fun RemoteBoxScore.RemoteGame.RemoteTeam.toBoxScoreTeam(): BoxScore.BoxScoreTeam {
    return BoxScore.BoxScoreTeam(
        team = teamId.let { NBATeam.getTeamById(it) },
        score = score.getOrZero(),
        periods = periods?.map { it.toPeriod() } ?: emptyList(),
        players = players?.mapNotNull { it.toPlayer() } ?: emptyList(),
        statistics = statistics?.toStatistics()
    )
}

private fun Double.toPercentage(): Double = ((this * PercentageMultiplier).toInt() / PercentageDivider).decimalFormat()
