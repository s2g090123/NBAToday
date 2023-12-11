package com.jiachian.nbatoday.compose.screen.label

import com.jiachian.nbatoday.NA
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreTeamLabel
import com.jiachian.nbatoday.compose.screen.team.TeamPlayerLabel
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.utils.decimalFormat

object LabelHelper {
    fun createScoreLabel(): Array<ScoreLabel> {
        return ScoreLabel.values()
    }

    fun createScoreTeamLabel(): Array<ScoreTeamLabel> {
        return ScoreTeamLabel.values()
    }

    fun createScoreLeaderLabel(): Array<ScoreLeaderLabel> {
        return ScoreLeaderLabel.values()
    }

    fun createTeamPlayerLabel(): Array<TeamPlayerLabel> {
        return TeamPlayerLabel.values()
    }

    fun createPlayerStatsLabel(): Array<PlayerStatsLabel> {
        return PlayerStatsLabel.values()
    }

    fun getValueByLabel(
        label: ScoreLabel,
        stats: BoxScore.BoxScoreTeam.Player.Statistics?
    ): String {
        stats ?: return NA
        return when (label) {
            ScoreLabel.MIN -> stats.minutes
            ScoreLabel.FGMA -> stats.fieldGoalProportion
            ScoreLabel.PMA3 -> stats.threePointProportion
            ScoreLabel.FTMA -> stats.freeThrowProportion
            ScoreLabel.PLUSMINUS -> stats.plusMinusPoints
            ScoreLabel.OREB -> stats.reboundsOffensive
            ScoreLabel.DREB -> stats.reboundsDefensive
            ScoreLabel.REB -> stats.reboundsTotal
            ScoreLabel.AST -> stats.assists
            ScoreLabel.PF -> stats.foulsPersonal
            ScoreLabel.STL -> stats.steals
            ScoreLabel.TOV -> stats.turnovers
            ScoreLabel.BS -> stats.blocks
            ScoreLabel.BA -> stats.blocksReceived
            ScoreLabel.PTS -> stats.points
            ScoreLabel.EFF -> stats.efficiency
        }.toString()
    }

    fun getValueByLabel(
        label: ScoreTeamLabel,
        stats: BoxScore.BoxScoreTeam.Statistics?
    ): String {
        stats ?: return NA
        return when (label) {
            ScoreTeamLabel.PTS -> stats.points
            ScoreTeamLabel.FG -> stats.fieldGoalsFormat
            ScoreTeamLabel.TP -> stats.twoPointsFormat
            ScoreTeamLabel.P3 -> stats.threePointsFormat
            ScoreTeamLabel.FT -> stats.freeThrowFormat
            ScoreTeamLabel.REB -> stats.reboundsTotal
            ScoreTeamLabel.DREB -> stats.reboundsDefensive
            ScoreTeamLabel.OREB -> stats.reboundsOffensive
            ScoreTeamLabel.AST -> stats.assists
            ScoreTeamLabel.BLK -> stats.blocks
            ScoreTeamLabel.STL -> stats.steals
            ScoreTeamLabel.TO -> stats.turnovers
            ScoreTeamLabel.FASTBREAK -> stats.pointsFastBreak
            ScoreTeamLabel.POINTSTURNOVERS -> stats.pointsFromTurnovers
            ScoreTeamLabel.POINTSINPAINT -> stats.pointsInThePaint
            ScoreTeamLabel.POINTSSECONDCHANCE -> stats.pointsSecondChance
            ScoreTeamLabel.BENCHPOINTS -> stats.benchPoints
            ScoreTeamLabel.PF -> stats.foulsPersonal
            ScoreTeamLabel.TF -> stats.foulsTechnical
        }.toString()
    }

    fun getValueByLabel(
        label: ScoreLeaderLabel,
        player: BoxScore.BoxScoreTeam.Player?
    ): String {
        player ?: return NA
        val stats = player.statistics
        return when (label) {
            ScoreLeaderLabel.NAME -> player.nameAbbr
            ScoreLeaderLabel.POSITION -> player.position
            ScoreLeaderLabel.MIN -> stats.minutes
            ScoreLeaderLabel.PTS -> stats.points
            ScoreLeaderLabel.PLUSMINUS -> stats.plusMinusPoints
            ScoreLeaderLabel.FG -> stats.fieldGoalsFormat
            ScoreLeaderLabel.P2 -> stats.twoPointsFormat
            ScoreLeaderLabel.P3 -> stats.threePointsFormat
            ScoreLeaderLabel.FT -> stats.freeThrowFormat
            ScoreLeaderLabel.REB -> stats.reboundsTotal
            ScoreLeaderLabel.DREB -> stats.reboundsDefensive
            ScoreLeaderLabel.OREB -> stats.reboundsOffensive
            ScoreLeaderLabel.AST -> stats.assists
            ScoreLeaderLabel.BLK -> stats.blocks
            ScoreLeaderLabel.STL -> stats.steals
            ScoreLeaderLabel.TO -> stats.turnovers
            ScoreLeaderLabel.PF -> stats.foulsPersonal
            ScoreLeaderLabel.TF -> stats.foulsTechnical
        }.toString()
    }

    fun getValueByLabel(
        label: PlayerStatsLabel,
        stats: Player.PlayerStats.Stats
    ): String {
        return when (label) {
            PlayerStatsLabel.GP -> stats.gamePlayed
            PlayerStatsLabel.W -> stats.win
            PlayerStatsLabel.L -> stats.lose
            PlayerStatsLabel.WINP -> stats.winPercentage
            PlayerStatsLabel.PTS -> stats.points.toDouble() / stats.gamePlayed
            PlayerStatsLabel.FGM -> stats.fieldGoalsMade.toDouble() / stats.gamePlayed
            PlayerStatsLabel.FGA -> stats.fieldGoalsAttempted.toDouble() / stats.gamePlayed
            PlayerStatsLabel.FGP -> stats.fieldGoalsPercentage
            PlayerStatsLabel.PM3 -> stats.threePointersMade.toDouble() / stats.gamePlayed
            PlayerStatsLabel.PA3 -> stats.threePointersAttempted.toDouble() / stats.gamePlayed
            PlayerStatsLabel.PP3 -> stats.threePointersPercentage
            PlayerStatsLabel.FTM -> stats.freeThrowsMade.toDouble() / stats.gamePlayed
            PlayerStatsLabel.FTA -> stats.freeThrowsAttempted.toDouble() / stats.gamePlayed
            PlayerStatsLabel.FTP -> stats.freeThrowsPercentage
            PlayerStatsLabel.OREB -> stats.reboundsOffensive.toDouble() / stats.gamePlayed
            PlayerStatsLabel.DREB -> stats.reboundsDefensive.toDouble() / stats.gamePlayed
            PlayerStatsLabel.REB -> stats.reboundsTotal.toDouble() / stats.gamePlayed
            PlayerStatsLabel.AST -> stats.assists.toDouble() / stats.gamePlayed
            PlayerStatsLabel.TOV -> stats.turnovers.toDouble() / stats.gamePlayed
            PlayerStatsLabel.STL -> stats.steals.toDouble() / stats.gamePlayed
            PlayerStatsLabel.BLK -> stats.blocks.toDouble() / stats.gamePlayed
            PlayerStatsLabel.PF -> stats.foulsPersonal.toDouble() / stats.gamePlayed
            PlayerStatsLabel.PLUSMINUS -> stats.plusMinus
        }.let {
            if (it is Double) {
                it.decimalFormat()
            } else {
                it
            }
        }.toString()
    }
}
