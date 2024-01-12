package com.jiachian.nbatoday.compose.screen.label

import com.jiachian.nbatoday.NA
import com.jiachian.nbatoday.compose.screen.home.standing.models.StandingLabel
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsLabel
import com.jiachian.nbatoday.compose.screen.player.models.PlayerTableLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScoreTeamLabel
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerLabel
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.utils.decimalFormat

/**
 * Helper object for obtaining values based on different labels for various UI components.
 */
object LabelHelper {
    fun getValueByLabel(
        label: StandingLabel,
        stats: Team,
    ): String {
        return when (label) {
            StandingLabel.GP -> stats.gamePlayed
            StandingLabel.W -> stats.win
            StandingLabel.L -> stats.lose
            StandingLabel.WINP -> stats.winPercentage
            StandingLabel.PTS -> stats.pointsAverage
            StandingLabel.FGP -> stats.fieldGoalsPercentage
            StandingLabel.PP3 -> stats.threePointersPercentage
            StandingLabel.FTP -> stats.freeThrowsPercentage
            StandingLabel.OREB -> stats.reboundsOffensiveAverage
            StandingLabel.DREB -> stats.reboundsDefensiveAverage
            StandingLabel.AST -> stats.assistsAverage
            StandingLabel.TOV -> stats.turnoversAverage
            StandingLabel.STL -> stats.stealsAverage
            StandingLabel.BLK -> stats.blocksAverage
        }.let {
            if (it is Double) {
                it.decimalFormat()
            } else {
                it
            }
        }.toString()
    }

    fun getValueByLabel(
        label: BoxScorePlayerLabel,
        stats: BoxScore.BoxScoreTeam.Player.Statistics
    ): String {
        return when (label) {
            BoxScorePlayerLabel.MIN -> stats.minutes
            BoxScorePlayerLabel.FGMA -> stats.fieldGoalProportion
            BoxScorePlayerLabel.PMA3 -> stats.threePointProportion
            BoxScorePlayerLabel.FTMA -> stats.freeThrowProportion
            BoxScorePlayerLabel.PLUSMINUS -> stats.plusMinusPoints
            BoxScorePlayerLabel.OREB -> stats.reboundsOffensive
            BoxScorePlayerLabel.DREB -> stats.reboundsDefensive
            BoxScorePlayerLabel.REB -> stats.reboundsTotal
            BoxScorePlayerLabel.AST -> stats.assists
            BoxScorePlayerLabel.PF -> stats.foulsPersonal
            BoxScorePlayerLabel.STL -> stats.steals
            BoxScorePlayerLabel.TOV -> stats.turnovers
            BoxScorePlayerLabel.BS -> stats.blocks
            BoxScorePlayerLabel.BA -> stats.blocksReceived
            BoxScorePlayerLabel.PTS -> stats.points
            BoxScorePlayerLabel.EFF -> stats.efficiency
        }.toString()
    }

    fun getValueByLabel(
        label: BoxScoreTeamLabel,
        stats: BoxScore.BoxScoreTeam.Statistics?
    ): String {
        stats ?: return NA
        return when (label) {
            BoxScoreTeamLabel.PTS -> stats.points
            BoxScoreTeamLabel.FG -> stats.fieldGoalsFormat
            BoxScoreTeamLabel.TP -> stats.twoPointsFormat
            BoxScoreTeamLabel.P3 -> stats.threePointsFormat
            BoxScoreTeamLabel.FT -> stats.freeThrowFormat
            BoxScoreTeamLabel.REB -> stats.reboundsTotal
            BoxScoreTeamLabel.DREB -> stats.reboundsDefensive
            BoxScoreTeamLabel.OREB -> stats.reboundsOffensive
            BoxScoreTeamLabel.AST -> stats.assists
            BoxScoreTeamLabel.BLK -> stats.blocks
            BoxScoreTeamLabel.STL -> stats.steals
            BoxScoreTeamLabel.TO -> stats.turnovers
            BoxScoreTeamLabel.FASTBREAK -> stats.pointsFastBreak
            BoxScoreTeamLabel.POINTSTURNOVERS -> stats.pointsFromTurnovers
            BoxScoreTeamLabel.POINTSINPAINT -> stats.pointsInThePaint
            BoxScoreTeamLabel.POINTSSECONDCHANCE -> stats.pointsSecondChance
            BoxScoreTeamLabel.BENCHPOINTS -> stats.benchPoints
            BoxScoreTeamLabel.PF -> stats.foulsPersonal
            BoxScoreTeamLabel.TF -> stats.foulsTechnical
        }.toString()
    }

    fun getValueByLabel(
        label: BoxScoreLeaderLabel,
        player: BoxScore.BoxScoreTeam.Player?
    ): String {
        player ?: return NA
        val stats = player.statistics
        return when (label) {
            BoxScoreLeaderLabel.NAME -> player.nameAbbr
            BoxScoreLeaderLabel.POSITION -> player.position
            BoxScoreLeaderLabel.MIN -> stats.minutes
            BoxScoreLeaderLabel.PTS -> stats.points
            BoxScoreLeaderLabel.PLUSMINUS -> stats.plusMinusPoints
            BoxScoreLeaderLabel.FG -> stats.fieldGoalsFormat
            BoxScoreLeaderLabel.P2 -> stats.twoPointsFormat
            BoxScoreLeaderLabel.P3 -> stats.threePointsFormat
            BoxScoreLeaderLabel.FT -> stats.freeThrowFormat
            BoxScoreLeaderLabel.REB -> stats.reboundsTotal
            BoxScoreLeaderLabel.DREB -> stats.reboundsDefensive
            BoxScoreLeaderLabel.OREB -> stats.reboundsOffensive
            BoxScoreLeaderLabel.AST -> stats.assists
            BoxScoreLeaderLabel.BLK -> stats.blocks
            BoxScoreLeaderLabel.STL -> stats.steals
            BoxScoreLeaderLabel.TO -> stats.turnovers
            BoxScoreLeaderLabel.PF -> stats.foulsPersonal
            BoxScoreLeaderLabel.TF -> stats.foulsTechnical
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
            PlayerStatsLabel.PTS -> stats.pointsAverage
            PlayerStatsLabel.FGM -> stats.fieldGoalsMadeAverage
            PlayerStatsLabel.FGA -> stats.fieldGoalsAttemptedAverage
            PlayerStatsLabel.FGP -> stats.fieldGoalsPercentage
            PlayerStatsLabel.PM3 -> stats.threePointersMadeAverage
            PlayerStatsLabel.PA3 -> stats.threePointersAttemptedAverage
            PlayerStatsLabel.PP3 -> stats.threePointersPercentage
            PlayerStatsLabel.FTM -> stats.freeThrowsMadeAverage
            PlayerStatsLabel.FTA -> stats.freeThrowsAttemptedAverage
            PlayerStatsLabel.FTP -> stats.freeThrowsPercentage
            PlayerStatsLabel.OREB -> stats.reboundsOffensiveAverage
            PlayerStatsLabel.DREB -> stats.reboundsDefensiveAverage
            PlayerStatsLabel.REB -> stats.reboundsTotalAverage
            PlayerStatsLabel.AST -> stats.assistsAverage
            PlayerStatsLabel.TOV -> stats.turnoversAverage
            PlayerStatsLabel.STL -> stats.stealsAverage
            PlayerStatsLabel.BLK -> stats.blocksAverage
            PlayerStatsLabel.PF -> stats.foulsPersonalAverage
            PlayerStatsLabel.PLUSMINUS -> stats.plusMinus
        }.let {
            if (it is Double) {
                it.decimalFormat()
            } else {
                it
            }
        }.toString()
    }

    fun getValueByLabel(
        label: TeamPlayerLabel,
        stats: TeamPlayer
    ): String {
        return when (label) {
            TeamPlayerLabel.GP -> stats.gamePlayed
            TeamPlayerLabel.W -> stats.win
            TeamPlayerLabel.L -> stats.lose
            TeamPlayerLabel.WINP -> stats.winPercentage
            TeamPlayerLabel.PTS -> stats.pointsAverage
            TeamPlayerLabel.FGM -> stats.fieldGoalsMadeAverage
            TeamPlayerLabel.FGA -> stats.fieldGoalsAttemptedAverage
            TeamPlayerLabel.FGP -> stats.fieldGoalsPercentage
            TeamPlayerLabel.PM3 -> stats.threePointersMadeAverage
            TeamPlayerLabel.PA3 -> stats.threePointersAttemptedAverage
            TeamPlayerLabel.PP3 -> stats.threePointersPercentage
            TeamPlayerLabel.FTM -> stats.freeThrowsMadeAverage
            TeamPlayerLabel.FTA -> stats.freeThrowsAttemptedAverage
            TeamPlayerLabel.FTP -> stats.freeThrowsPercentage
            TeamPlayerLabel.OREB -> stats.reboundsOffensiveAverage
            TeamPlayerLabel.DREB -> stats.reboundsDefensiveAverage
            TeamPlayerLabel.REB -> stats.reboundsTotalAverage
            TeamPlayerLabel.AST -> stats.assistsAverage
            TeamPlayerLabel.TOV -> stats.turnoversAverage
            TeamPlayerLabel.STL -> stats.stealsAverage
            TeamPlayerLabel.BLK -> stats.blocksAverage
            TeamPlayerLabel.PF -> stats.foulsPersonalAverage
            TeamPlayerLabel.PLUSMINUS -> stats.plusMinus
        }.let {
            if (it is Double) {
                it.decimalFormat()
            } else {
                it
            }
        }.toString()
    }

    fun getValueByLabel(
        label: PlayerTableLabel,
        info: Player.PlayerInfo
    ): String {
        return when (label) {
            PlayerTableLabel.PPG -> info.points
            PlayerTableLabel.RPG -> info.rebounds
            PlayerTableLabel.APG -> info.assists
            PlayerTableLabel.PIE -> info.impact
            PlayerTableLabel.HEIGHT -> info.heightFormatted
            PlayerTableLabel.WEIGHT -> info.weightFormatted
            PlayerTableLabel.COUNTRY -> info.country
            PlayerTableLabel.LAST_ATTENDED -> info.school
            PlayerTableLabel.AGE -> info.playerAge
            PlayerTableLabel.BIRTHDATE -> info.birthDate
            PlayerTableLabel.DRAFT -> info.draftFormatted
            PlayerTableLabel.EXPERIENCE -> info.seasonExperience
        }.toString()
    }
}
