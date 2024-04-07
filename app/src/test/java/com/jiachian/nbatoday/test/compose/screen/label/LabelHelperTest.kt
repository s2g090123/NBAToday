package com.jiachian.nbatoday.test.compose.screen.label

import com.jiachian.nbatoday.boxscore.ui.leader.model.BoxScoreLeaderLabel
import com.jiachian.nbatoday.boxscore.ui.player.model.BoxScorePlayerLabel
import com.jiachian.nbatoday.boxscore.ui.team.model.BoxScoreTeamLabel
import com.jiachian.nbatoday.common.data.NA
import com.jiachian.nbatoday.data.local.BoxScoreGenerator
import com.jiachian.nbatoday.data.local.PlayerGenerator
import com.jiachian.nbatoday.data.local.TeamGenerator
import com.jiachian.nbatoday.data.local.TeamPlayerGenerator
import com.jiachian.nbatoday.home.standing.ui.model.StandingLabel
import com.jiachian.nbatoday.player.ui.model.PlayerStatsLabel
import com.jiachian.nbatoday.player.ui.model.PlayerTableLabel
import com.jiachian.nbatoday.team.ui.player.model.TeamPlayerLabel
import com.jiachian.nbatoday.utils.LabelHelper
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.decimalFormat
import com.jiachian.nbatoday.utils.getOrAssert
import org.junit.Test

class LabelHelperTest {
    @Test
    fun `getValueByLabel(StandingLabel) expects correct`() {
        val team = TeamGenerator.getHome()
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.GP, team),
            team.gamePlayed.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.W, team),
            team.win.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.L, team),
            team.lose.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.WINP, team),
            team.winPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.PTS, team),
            team.pointsAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.FGP, team),
            team.fieldGoalsPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.PP3, team),
            team.threePointersPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.FTP, team),
            team.freeThrowsPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.OREB, team),
            team.reboundsOffensiveAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.DREB, team),
            team.reboundsDefensiveAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.AST, team),
            team.assistsAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.TOV, team),
            team.turnoversAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.STL, team),
            team.stealsAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(StandingLabel.BLK, team),
            team.blocksAverage.decimalFormat().toString()
        )
    }

    @Test
    fun `getValueByLabel(BoxScorePlayerLabel) expects correct`() {
        val player = BoxScoreGenerator.getFinal().homeTeam.players.first().statistics
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.MIN, player),
            player.minutes
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.FGMA, player),
            player.fieldGoalProportion
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.PMA3, player),
            player.threePointProportion
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.FTMA, player),
            player.freeThrowProportion
        )
        assertIs(
            LabelHelper.getValueByLabel(
                BoxScorePlayerLabel.PLUSMINUS, player
            ),
            player.plusMinusPoints.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.OREB, player),
            player.reboundsOffensive.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.DREB, player),
            player.reboundsDefensive.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.REB, player),
            player.reboundsTotal.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.AST, player),
            player.assists.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.PF, player),
            player.foulsPersonal.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.STL, player),
            player.steals.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.TOV, player),
            player.turnovers.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.BS, player),
            player.blocks.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.BA, player),
            player.blocksReceived.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.PTS, player),
            player.points.toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(BoxScorePlayerLabel.EFF, player),
            player.efficiency.toString()
        )
    }

    @Test
    fun `getValueByLabel(BoxScoreTeamLabel) expects correct`() {
        val team = BoxScoreGenerator.getFinal().homeTeam.statistics.getOrAssert()
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.PTS, null), NA)
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.PTS, team), team.points.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.FG, team), team.fieldGoalsFormat)
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.TP, team), team.twoPointsFormat)
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.P3, team), team.threePointsFormat)
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.FT, team), team.freeThrowFormat)
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.REB, team), team.reboundsTotal.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.DREB, team), team.reboundsDefensive.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.OREB, team), team.reboundsOffensive.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.AST, team), team.assists.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.BLK, team), team.blocks.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.STL, team), team.steals.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.PTS, team), team.points.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.TO, team), team.turnovers.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.FASTBREAK, team), team.pointsFastBreak.toString())
        assertIs(
            LabelHelper.getValueByLabel(BoxScoreTeamLabel.POINTSTURNOVERS, team),
            team.pointsFromTurnovers.toString()
        )
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.POINTSINPAINT, team), team.pointsInThePaint.toString())
        assertIs(
            LabelHelper.getValueByLabel(BoxScoreTeamLabel.POINTSSECONDCHANCE, team),
            team.pointsSecondChance.toString()
        )
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.BENCHPOINTS, team), team.benchPoints.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.PF, team), team.foulsPersonal.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreTeamLabel.TF, team), team.foulsTechnical.toString())
    }

    @Test
    fun `getValueByLabel(BoxScoreLeader) expects correct`() {
        val player = BoxScoreGenerator.getFinal().homeTeam.getMostPointsPlayer().getOrAssert()
        val stats = player.statistics
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.NAME, null), NA)
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.NAME, player), player.nameAbbr)
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.POSITION, player), player.position)
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.MIN, player), stats.minutes)
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.PTS, player), stats.points.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.PLUSMINUS, player), stats.plusMinusPoints.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.FG, player), stats.fieldGoalsFormat)
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.P2, player), stats.twoPointsFormat)
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.P3, player), stats.threePointsFormat)
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.FT, player), stats.freeThrowFormat)
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.REB, player), stats.reboundsTotal.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.DREB, player), stats.reboundsDefensive.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.OREB, player), stats.reboundsOffensive.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.AST, player), stats.assists.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.BLK, player), stats.blocks.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.STL, player), stats.steals.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.TO, player), stats.turnovers.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.PF, player), stats.foulsPersonal.toString())
        assertIs(LabelHelper.getValueByLabel(BoxScoreLeaderLabel.TF, player), stats.foulsTechnical.toString())
    }

    @Test
    fun `getValueByLabel(PlayerStatsLabel) expects correct`() {
        val stats = PlayerGenerator.getHome().stats.stats.first()
        assertIs(LabelHelper.getValueByLabel(PlayerStatsLabel.GP, stats), stats.gamePlayed.toString())
        assertIs(LabelHelper.getValueByLabel(PlayerStatsLabel.W, stats), stats.win.toString())
        assertIs(LabelHelper.getValueByLabel(PlayerStatsLabel.L, stats), stats.lose.toString())
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.WINP, stats),
            stats.winPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.PTS, stats),
            stats.pointsAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.FGM, stats),
            stats.fieldGoalsMadeAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.FGA, stats),
            stats.fieldGoalsAttemptedAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.FGP, stats),
            stats.fieldGoalsPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.PM3, stats),
            stats.threePointersMadeAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.PA3, stats),
            stats.threePointersAttemptedAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.PP3, stats),
            stats.threePointersPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.FTM, stats),
            stats.freeThrowsMadeAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.FTA, stats),
            stats.freeThrowsAttemptedAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.FTP, stats),
            stats.freeThrowsPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.OREB, stats),
            stats.reboundsOffensiveAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.DREB, stats),
            stats.reboundsDefensiveAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.REB, stats),
            stats.reboundsTotalAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.AST, stats),
            stats.assistsAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.TOV, stats),
            stats.turnoversAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.STL, stats),
            stats.stealsAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.BLK, stats),
            stats.blocksAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.PF, stats),
            stats.foulsPersonalAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(PlayerStatsLabel.PLUSMINUS, stats),
            stats.plusMinus.toString()
        )
    }

    @Test
    fun `getValueByLabel(TeamPlayerLabel) expects correct`() {
        val stats = TeamPlayerGenerator.getHome().first()
        assertIs(LabelHelper.getValueByLabel(TeamPlayerLabel.GP, stats), stats.gamePlayed.toString())
        assertIs(LabelHelper.getValueByLabel(TeamPlayerLabel.W, stats), stats.win.toString())
        assertIs(LabelHelper.getValueByLabel(TeamPlayerLabel.L, stats), stats.lose.toString())
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.WINP, stats),
            stats.winPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.PTS, stats),
            stats.pointsAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.FGM, stats),
            stats.fieldGoalsMadeAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.FGA, stats),
            stats.fieldGoalsAttemptedAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.FGP, stats),
            stats.fieldGoalsPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.PM3, stats),
            stats.threePointersMadeAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.PA3, stats),
            stats.threePointersAttemptedAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.PP3, stats),
            stats.threePointersPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.FTM, stats),
            stats.freeThrowsMadeAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.FTA, stats),
            stats.freeThrowsAttemptedAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.FTP, stats),
            stats.freeThrowsPercentage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.OREB, stats),
            stats.reboundsOffensiveAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.DREB, stats),
            stats.reboundsDefensiveAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.REB, stats),
            stats.reboundsTotalAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.AST, stats),
            stats.assistsAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.TOV, stats),
            stats.turnoversAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.STL, stats),
            stats.stealsAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.BLK, stats),
            stats.blocksAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.PF, stats),
            stats.foulsPersonalAverage.decimalFormat().toString()
        )
        assertIs(
            LabelHelper.getValueByLabel(TeamPlayerLabel.PLUSMINUS, stats),
            stats.plusMinus.toString()
        )
    }

    @Test
    fun `getValueByLabel(PlayerTableLabel) expects correct`() {
        val info = PlayerGenerator.getHome().info
        PlayerTableLabel.values().forEach { label ->
            val expected = when (label) {
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
            LabelHelper
                .getValueByLabel(label, info)
                .assertIs(expected)
        }
    }
}
