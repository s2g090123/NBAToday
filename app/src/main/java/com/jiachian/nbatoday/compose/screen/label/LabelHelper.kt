package com.jiachian.nbatoday.compose.screen.label

import com.jiachian.nbatoday.NA
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreTeamLabel
import com.jiachian.nbatoday.compose.screen.team.TeamPlayerLabel
import com.jiachian.nbatoday.data.local.score.GameBoxScore

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

    fun getValueByLabel(
        label: ScoreLabel,
        stats: GameBoxScore.BoxScoreTeam.Player.Statistics?
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
        stats: GameBoxScore.BoxScoreTeam.Statistics?
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
        player: GameBoxScore.BoxScoreTeam.Player?
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
}
