package com.jiachian.nbatoday.compose.screen.score

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.data.BoxScoreFactory
import com.jiachian.nbatoday.data.NbaGameFactory
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.score.PlayerActiveStatus
import com.jiachian.nbatoday.utils.assertPopupExist
import com.jiachian.nbatoday.utils.getOrAssert
import com.jiachian.nbatoday.utils.onNodeWithMergedTag
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onPopup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoxScoreScreenTest : BaseAndroidTest() {

    private lateinit var viewModel: BoxScoreViewModel
    private lateinit var repository: TestRepository
    private var isBack = false

    private var showPlayerId: Int? = null
    private val game = NbaGameFactory.getFinalGame()
    private val score = BoxScoreFactory.getFinalGameBoxScore()

    @Before
    fun setup() = runTest {
        repository = TestRepository()
        viewModel = BoxScoreViewModel(
            game = game,
            repository = repository,
            showPlayerCareer = {
                showPlayerId = it
            },
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider
        )
        composeTestRule.setContent {
            BoxScoreScreen(
                viewModel = viewModel,
                onBack = { isBack = true }
            )
        }
    }

    @After
    fun teardown() {
        repository.clear()
        isBack = false
        showPlayerId = null
    }

    @Test
    fun boxScore_clickBack_checksIsBack() {
        composeTestRule
            .onNodeWithMergedTag("ScoreScreen_Btn_Back")
            .performClick()
        assertThat(isBack, `is`(true))
    }

    @Test
    fun boxScore_checksGameInfoUI() {
        val home = score.homeTeam.getOrAssert()
        val away = score.awayTeam.getOrAssert()
        composeTestRule
            .onNodeWithMergedTag("ScoreScreen_Text_Date")
            .assertTextEquals(score.gameDate)
        composeTestRule
            .onNodeWithMergedTag("ScoreTotal_Text_ScoreComparison")
            .assertTextEquals("${home.score} - ${away.score}")
        composeTestRule
            .onNodeWithMergedTag("ScoreTotal_Text_Status")
            .assertTextEquals(game.gameStatusText)
        composeTestRule
            .onNodeWithMergedTag("ScoreTotal_Text_HomeName")
            .assertTextEquals(home.teamName)
        composeTestRule
            .onNodeWithMergedTag("ScoreTotal_Text_AwayName")
            .assertTextEquals(away.teamName)
        val scorePeriod = composeTestRule
            .onNodeWithMergedTag("ScoreScreen_ScorePeriod")
        val homePeriod = scorePeriod
            .onNodeWithTag("ScorePeriod_Box_Score", 0)
        val awayPeriod = scorePeriod
            .onNodeWithTag("ScorePeriod_Box_Score", 1)
        homePeriod.apply {
            onNodeWithTag("ScorePeriod_Text_TeamName")
                .assertTextEquals(home.teamName)
            onNodeWithTag("ScorePeriod_Text_Score", 0)
                .assertTextEquals(home.periods[0].score.toString())
            onNodeWithTag("ScorePeriod_Text_Score", 1)
                .assertTextEquals(home.periods[1].score.toString())
            onNodeWithTag("ScorePeriod_Text_Score", 2)
                .assertTextEquals(home.periods[2].score.toString())
            onNodeWithTag("ScorePeriod_Text_Score", 3)
                .assertTextEquals(home.periods[3].score.toString())
            onNodeWithTag("ScorePeriod_Text_ScoreTotal")
                .assertTextEquals(home.score.toString())
        }
        awayPeriod.apply {
            onNodeWithTag("ScorePeriod_Text_TeamName")
                .assertTextEquals(away.teamName)
            onNodeWithTag("ScorePeriod_Text_Score", 0)
                .assertTextEquals(away.periods[0].score.toString())
            onNodeWithTag("ScorePeriod_Text_Score", 1)
                .assertTextEquals(away.periods[1].score.toString())
            onNodeWithTag("ScorePeriod_Text_Score", 2)
                .assertTextEquals(away.periods[2].score.toString())
            onNodeWithTag("ScorePeriod_Text_Score", 3)
                .assertTextEquals(away.periods[3].score.toString())
            onNodeWithTag("ScorePeriod_Text_ScoreTotal")
                .assertTextEquals(away.score.toString())
        }
    }

    @Test
    fun boxScore_checksHomePlayerStatsUI() {
        val home = score.homeTeam.getOrAssert()
        composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Tab_Home")
            .performClick()
        val playerStats = composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Pager")
            .onNodeWithTag("ScoreDetail_PlayerStatistics_Home")
        playerStats
            .onNodeWithTag("PlayerStatistics_LC_Players")
            .onNodeWithTag("PlayerStatistics_Column_Player")
            .onNodeWithTag("PlayerStatistics_Text_PlayerName")
            .assertTextEquals(home.players.first().nameAbbr)
        playerStats
            .onNodeWithTag("PlayerStatistics_Column_Stats")
            .onNodeWithTag("PlayerStatistics_Row_Labels")
            .apply {
                viewModel.scoreLabel.value.forEachIndexed { index, label ->
                    onNodeWithTag("PlayerStatistics_Box_Label", index)
                        .performScrollTo()
                        .assertIsDisplayed()
                        .performClick()
                    composeTestRule
                        .assertPopupExist()
                        .onPopup()
                        .onNodeWithTag("LabelAboutPopup_Text_About")
                        .assertTextEquals(getLabelHint(label))
                }
            }
        playerStats
            .onNodeWithTag("PlayerStatistics_Column_Stats")
            .onNodeWithTag("PlayerStatistics_LC_PlayerStats")
            .onNodeWithTag("PlayerStatistics_Row_PlayerStats")
            .apply {
                home.players.forEachIndexed { index, player ->
                    onNodeWithTag("PlayerStatistics_Text_PlayerPosition", index)
                        .assertTextEquals(player.position)
                    if (player.status == PlayerActiveStatus.INACTIVE) {
                        onNodeWithTag("PlayerStatistics_Text_NotPlay")
                            .assertIsDisplayed()
                        onNodeWithTag("PlayerStatistics_Text_NotPlayReason")
                            .assertIsDisplayed()
                            .assertTextEquals(player.notPlayingReason ?: "")
                        onNodeWithTag("PlayerStatistics_Text_PlayerStats")
                            .assertDoesNotExist()
                    } else {
                        onNodeWithTag("PlayerStatistics_Text_NotPlay")
                            .assertDoesNotExist()
                        onNodeWithTag("PlayerStatistics_Text_NotPlayReason")
                            .assertDoesNotExist()
                        viewModel.scoreLabel.value.forEachIndexed { labelIndex, label ->
                            onNodeWithTag("PlayerStatistics_Text_PlayerStats", labelIndex)
                                .performScrollTo()
                                .assertTextEquals(getPlayerStats(label, player))
                        }
                    }
                }
            }
    }

    @Test
    fun boxScore_checksAwayPlayerStatsUI() {
        val away = score.awayTeam.getOrAssert()
        composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Tab_Away")
            .performClick()
        val playerStats = composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Pager")
            .onNodeWithTag("ScoreDetail_PlayerStatistics_Away")
        playerStats
            .onNodeWithTag("PlayerStatistics_LC_Players")
            .onNodeWithTag("PlayerStatistics_Column_Player")
            .onNodeWithTag("PlayerStatistics_Text_PlayerName")
            .assertTextEquals(away.players.first().nameAbbr)
        playerStats
            .onNodeWithTag("PlayerStatistics_Column_Stats")
            .onNodeWithTag("PlayerStatistics_Row_Labels")
            .apply {
                viewModel.scoreLabel.value.forEachIndexed { index, label ->
                    onNodeWithTag("PlayerStatistics_Box_Label", index)
                        .performScrollTo()
                        .assertIsDisplayed()
                        .performClick()
                    composeTestRule
                        .assertPopupExist()
                        .onPopup()
                        .onNodeWithTag("LabelAboutPopup_Text_About")
                        .assertTextEquals(getLabelHint(label))
                }
            }
        playerStats
            .onNodeWithTag("PlayerStatistics_Column_Stats")
            .onNodeWithTag("PlayerStatistics_LC_PlayerStats")
            .onNodeWithTag("PlayerStatistics_Row_PlayerStats")
            .apply {
                away.players.forEachIndexed { index, player ->
                    onNodeWithTag("PlayerStatistics_Text_PlayerPosition", index)
                        .assertTextEquals(player.position)
                    if (player.status == PlayerActiveStatus.INACTIVE) {
                        onNodeWithTag("PlayerStatistics_Text_NotPlay")
                            .assertIsDisplayed()
                        onNodeWithTag("PlayerStatistics_Text_NotPlayReason")
                            .assertIsDisplayed()
                            .assertTextEquals(player.notPlayingReason ?: "")
                        onNodeWithTag("PlayerStatistics_Text_PlayerStats")
                            .assertDoesNotExist()
                    } else {
                        onNodeWithTag("PlayerStatistics_Text_NotPlay")
                            .assertDoesNotExist()
                        onNodeWithTag("PlayerStatistics_Text_NotPlayReason")
                            .assertDoesNotExist()
                        viewModel.scoreLabel.value.forEachIndexed { labelIndex, label ->
                            onNodeWithTag("PlayerStatistics_Text_PlayerStats", labelIndex)
                                .performScrollTo()
                                .assertTextEquals(getPlayerStats(label, player))
                        }
                    }
                }
            }
    }

    @Test
    fun boxScore_checksTeamStatsUI() {
        val home = score.homeTeam.getOrAssert().statistics.getOrAssert()
        val away = score.awayTeam.getOrAssert().statistics.getOrAssert()
        composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Tab_TeamStats")
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Pager")
            .onNodeWithTag("ScoreDetail_TeamStatistics")
            .apply {
                onNodeWithTag("TeamStatistics_Text_HomePoints")
                    .assertTextEquals(home.points.toString())
                onNodeWithTag("TeamStatistics_Text_AwayPoints")
                    .assertTextEquals(away.points.toString())
                onNodeWithTag("TeamStatistics_Text_HomeFGM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_team_FGM,
                            home.fieldGoalsMade,
                            home.fieldGoalsAttempted,
                            home.fieldGoalsPercentage
                        )
                    )
                onNodeWithTag("TeamStatistics_Text_AwayFGM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_team_FGM,
                            away.fieldGoalsMade,
                            away.fieldGoalsAttempted,
                            away.fieldGoalsPercentage
                        )
                    )
                onNodeWithTag("TeamStatistics_Text_Home2PM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_team_2PM,
                            home.twoPointersMade,
                            home.twoPointersAttempted,
                            home.twoPointersPercentage
                        )
                    )
                onNodeWithTag("TeamStatistics_Text_Away2PM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_team_2PM,
                            away.twoPointersMade,
                            away.twoPointersAttempted,
                            away.twoPointersPercentage
                        )
                    )
                onNodeWithTag("TeamStatistics_Text_Home3PM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_team_3PM,
                            home.threePointersMade,
                            home.threePointersAttempted,
                            home.threePointersPercentage
                        )
                    )
                onNodeWithTag("TeamStatistics_Text_Away3PM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_team_3PM,
                            away.threePointersMade,
                            away.threePointersAttempted,
                            away.threePointersPercentage
                        )
                    )
                onNodeWithTag("TeamStatistics_Text_HomeFTM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_team_FTM,
                            home.freeThrowsMade,
                            home.freeThrowsAttempted,
                            home.freeThrowsPercentage
                        )
                    )
                onNodeWithTag("TeamStatistics_Text_AwayFTM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_team_FTM,
                            away.freeThrowsMade,
                            away.freeThrowsAttempted,
                            away.freeThrowsPercentage
                        )
                    )
                onNodeWithTag("TeamStatistics_Text_HomeRebPersonal")
                    .assertTextEquals(home.reboundsPersonal.toString())
                onNodeWithTag("TeamStatistics_Text_AwayRebPersonal")
                    .assertTextEquals(away.reboundsPersonal.toString())
                onNodeWithTag("TeamStatistics_Text_HomeRebDefensive")
                    .assertTextEquals(home.reboundsDefensive.toString())
                onNodeWithTag("TeamStatistics_Text_AwayRebDefensive")
                    .assertTextEquals(away.reboundsDefensive.toString())
                onNodeWithTag("TeamStatistics_Text_HomeRebOffensive")
                    .assertTextEquals(home.reboundsOffensive.toString())
                onNodeWithTag("TeamStatistics_Text_AwayRebOffensive")
                    .assertTextEquals(away.reboundsOffensive.toString())
                onNodeWithTag("TeamStatistics_Text_HomeAssists")
                    .assertTextEquals(home.assists.toString())
                onNodeWithTag("TeamStatistics_Text_AwayAssists")
                    .assertTextEquals(away.assists.toString())
                onNodeWithTag("TeamStatistics_Text_HomeBlocks")
                    .assertTextEquals(home.blocks.toString())
                onNodeWithTag("TeamStatistics_Text_AwayBlocks")
                    .assertTextEquals(away.blocks.toString())
                onNodeWithTag("TeamStatistics_Text_HomeSteals")
                    .assertTextEquals(home.steals.toString())
                onNodeWithTag("TeamStatistics_Text_AwaySteals")
                    .assertTextEquals(away.steals.toString())
                onNodeWithTag("TeamStatistics_Text_HomeTurnOvers")
                    .assertTextEquals(home.turnovers.toString())
                onNodeWithTag("TeamStatistics_Text_AwayTurnOvers")
                    .assertTextEquals(away.turnovers.toString())
                onNodeWithTag("TeamStatistics_Text_HomeFastPoints")
                    .assertTextEquals(home.pointsFastBreak.toString())
                onNodeWithTag("TeamStatistics_Text_AwayFastPoints")
                    .assertTextEquals(away.pointsFastBreak.toString())
                onNodeWithTag("TeamStatistics_Text_HomePointsTurnOver")
                    .assertTextEquals(home.pointsFromTurnovers.toString())
                onNodeWithTag("TeamStatistics_Text_AwayPointsTurnOver")
                    .assertTextEquals(away.pointsFromTurnovers.toString())
                onNodeWithTag("TeamStatistics_Text_HomePointsPaint")
                    .assertTextEquals(home.pointsInThePaint.toString())
                onNodeWithTag("TeamStatistics_Text_AwayPointsPaint")
                    .assertTextEquals(away.pointsInThePaint.toString())
                onNodeWithTag("TeamStatistics_Text_HomePointsSecond")
                    .assertTextEquals(home.pointsSecondChance.toString())
                onNodeWithTag("TeamStatistics_Text_AwayPointsSecond")
                    .assertTextEquals(away.pointsSecondChance.toString())
                onNodeWithTag("TeamStatistics_Text_HomeBenchPoints")
                    .assertTextEquals(home.benchPoints.toString())
                onNodeWithTag("TeamStatistics_Text_AwayBenchPoints")
                    .assertTextEquals(away.benchPoints.toString())
                onNodeWithTag("TeamStatistics_Text_HomeFoulsPersonal")
                    .assertTextEquals(home.foulsPersonal.toString())
                onNodeWithTag("TeamStatistics_Text_AwayFoulsPersonal")
                    .assertTextEquals(away.foulsPersonal.toString())
                onNodeWithTag("TeamStatistics_Text_HomeFoulsTechnical")
                    .assertTextEquals(home.foulsTechnical.toString())
                onNodeWithTag("TeamStatistics_Text_AwayFoulsTechnical")
                    .assertTextEquals(away.foulsTechnical.toString())
            }
    }

    @Test
    fun boxScore_checksLeaderStatsUI() {
        val homeLeader = viewModel.homeLeader.value.getOrAssert()
        val awayLeader = viewModel.awayLeader.value.getOrAssert()
        val homeStats = homeLeader.statistics.getOrAssert()
        val awayStats = awayLeader.statistics.getOrAssert()
        composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Tab_LeaderStats")
            .performClick()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Pager")
            .onNodeWithTag("ScoreDetail_LeaderStatistics")
            .apply {
                onNodeWithTag("LeaderStatistics_Text_HomePlayerName")
                    .assertTextEquals(homeLeader.nameAbbr)
                onNodeWithTag("LeaderStatistics_Text_AwayPlayerName")
                    .assertTextEquals(awayLeader.nameAbbr)
                onNodeWithTag("LeaderStatistics_Text_HomePlayerPosition")
                    .assertTextEquals(homeLeader.position)
                onNodeWithTag("LeaderStatistics_Text_AwayPlayerPosition")
                    .assertTextEquals(awayLeader.position)
                onNodeWithTag("LeaderStatistics_Text_HomeMinutes")
                    .assertTextEquals(homeStats.minutes)
                onNodeWithTag("LeaderStatistics_Text_AwayMinutes")
                    .assertTextEquals(awayStats.minutes)
                onNodeWithTag("LeaderStatistics_Text_HomePoints")
                    .assertTextEquals(homeStats.points.toString())
                onNodeWithTag("LeaderStatistics_Text_AwayPoints")
                    .assertTextEquals(awayStats.points.toString())
                onNodeWithTag("LeaderStatistics_Text_HomePlusMinus")
                    .assertTextEquals(homeStats.plusMinusPoints.toString())
                onNodeWithTag("LeaderStatistics_Text_AwayPlusMinus")
                    .assertTextEquals(awayStats.plusMinusPoints.toString())
                onNodeWithTag("LeaderStatistics_Text_HomeFGM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_player_FGM,
                            homeStats.fieldGoalsMade,
                            homeStats.fieldGoalsAttempted,
                            homeStats.fieldGoalsPercentage
                        )
                    )
                onNodeWithTag("LeaderStatistics_Text_AwayFGM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_player_FGM,
                            awayStats.fieldGoalsMade,
                            awayStats.fieldGoalsAttempted,
                            awayStats.fieldGoalsPercentage
                        )
                    )
                onNodeWithTag("LeaderStatistics_Text_Home2PM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_player_2PM,
                            homeStats.twoPointersMade,
                            homeStats.twoPointersAttempted,
                            homeStats.twoPointersPercentage
                        )
                    )
                onNodeWithTag("LeaderStatistics_Text_Away2PM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_player_2PM,
                            awayStats.twoPointersMade,
                            awayStats.twoPointersAttempted,
                            awayStats.twoPointersPercentage
                        )
                    )
                onNodeWithTag("LeaderStatistics_Text_Home3PM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_player_3PM,
                            homeStats.threePointersMade,
                            homeStats.threePointersAttempted,
                            homeStats.threePointersPercentage
                        )
                    )
                onNodeWithTag("LeaderStatistics_Text_Away3PM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_player_3PM,
                            awayStats.threePointersMade,
                            awayStats.threePointersAttempted,
                            awayStats.threePointersPercentage
                        )
                    )
                onNodeWithTag("LeaderStatistics_Text_HomeFTM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_player_FTM,
                            homeStats.freeThrowsMade,
                            homeStats.freeThrowsAttempted,
                            homeStats.freeThrowsPercentage
                        )
                    )
                onNodeWithTag("LeaderStatistics_Text_AwayFTM")
                    .assertTextEquals(
                        context.getString(
                            R.string.box_score_player_FTM,
                            awayStats.freeThrowsMade,
                            awayStats.freeThrowsAttempted,
                            awayStats.freeThrowsPercentage
                        )
                    )
                onNodeWithTag("LeaderStatistics_Text_HomeRebTotal")
                    .assertTextEquals(homeStats.reboundsTotal.toString())
                onNodeWithTag("LeaderStatistics_Text_AwayRebTotal")
                    .assertTextEquals(awayStats.reboundsTotal.toString())
                onNodeWithTag("LeaderStatistics_Text_HomeRebDefensive")
                    .assertTextEquals(homeStats.reboundsDefensive.toString())
                onNodeWithTag("LeaderStatistics_Text_AwayRebDefensive")
                    .assertTextEquals(awayStats.reboundsDefensive.toString())
                onNodeWithTag("LeaderStatistics_Text_HomeRebOffensive")
                    .assertTextEquals(homeStats.reboundsOffensive.toString())
                onNodeWithTag("LeaderStatistics_Text_AwayRebOffensive")
                    .assertTextEquals(awayStats.reboundsOffensive.toString())
                onNodeWithTag("LeaderStatistics_Text_HomeAssists")
                    .assertTextEquals(homeStats.assists.toString())
                onNodeWithTag("LeaderStatistics_Text_AwayAssists")
                    .assertTextEquals(awayStats.assists.toString())
                onNodeWithTag("LeaderStatistics_Text_HomeBlocks")
                    .assertTextEquals(homeStats.blocks.toString())
                onNodeWithTag("LeaderStatistics_Text_AwayBlocks")
                    .assertTextEquals(awayStats.blocks.toString())
                onNodeWithTag("LeaderStatistics_Text_HomeSteals")
                    .assertTextEquals(homeStats.steals.toString())
                onNodeWithTag("LeaderStatistics_Text_AwaySteals")
                    .assertTextEquals(awayStats.steals.toString())
                onNodeWithTag("LeaderStatistics_Text_HomeTurnOvers")
                    .assertTextEquals(homeStats.turnovers.toString())
                onNodeWithTag("LeaderStatistics_Text_AwayTurnOvers")
                    .assertTextEquals(awayStats.turnovers.toString())
                onNodeWithTag("LeaderStatistics_Text_HomeFoulPersonal")
                    .assertTextEquals(homeStats.foulsPersonal.toString())
                onNodeWithTag("LeaderStatistics_Text_AwayFoulPersonal")
                    .assertTextEquals(awayStats.foulsPersonal.toString())
                onNodeWithTag("LeaderStatistics_Text_HomeFoulTechnical")
                    .assertTextEquals(homeStats.foulsTechnical.toString())
                onNodeWithTag("LeaderStatistics_Text_AwayFoulTechnical")
                    .assertTextEquals(awayStats.foulsTechnical.toString())
            }
    }

    private fun getLabelHint(label: ScoreLabel): String {
        return when (label.text) {
            "MIN" -> context.getString(R.string.box_score_about_min)
            "FGM-A" -> context.getString(R.string.box_score_about_FGMA)
            "3PM-A" -> context.getString(R.string.box_score_about_3PMA)
            "FTM-A" -> context.getString(R.string.box_score_about_FTMA)
            "+/-" -> context.getString(R.string.box_score_about_plusMinus)
            "OR" -> context.getString(R.string.box_score_about_OR)
            "DR" -> context.getString(R.string.box_score_about_DR)
            "TR" -> context.getString(R.string.box_score_about_TR)
            "AS" -> context.getString(R.string.box_score_about_AS)
            "PF" -> context.getString(R.string.box_score_about_PF)
            "ST" -> context.getString(R.string.box_score_about_ST)
            "TO" -> context.getString(R.string.box_score_about_TO)
            "BS" -> context.getString(R.string.box_score_about_BS)
            "BA" -> context.getString(R.string.box_score_about_BA)
            "PTS" -> context.getString(R.string.box_score_about_PTS)
            "EFF" -> context.getString(R.string.box_score_about_EFF)
            else -> ""
        }
    }

    private fun getPlayerStats(
        label: ScoreLabel,
        player: GameBoxScore.BoxScoreTeam.Player
    ): String {
        val statistics = player.statistics.getOrAssert()
        return when (label.text) {
            "MIN" -> statistics.minutes
            "FGM-A" -> "${statistics.fieldGoalsMade}-${statistics.fieldGoalsAttempted}"
            "3PM-A" -> "${statistics.threePointersMade}-${statistics.threePointersAttempted}"
            "FTM-A" -> "${statistics.freeThrowsMade}-${statistics.freeThrowsAttempted}"
            "+/-" -> statistics.plusMinusPoints.toString()
            "OR" -> statistics.reboundsOffensive.toString()
            "DR" -> statistics.reboundsDefensive.toString()
            "TR" -> statistics.reboundsTotal.toString()
            "AS" -> statistics.assists.toString()
            "PF" -> statistics.foulsPersonal.toString()
            "ST" -> statistics.steals.toString()
            "TO" -> statistics.turnovers.toString()
            "BS" -> statistics.blocks.toString()
            "BA" -> statistics.blocksReceived.toString()
            "PTS" -> statistics.points.toString()
            "EFF" -> statistics.efficiency.toString()
            else -> ""
        }
    }
}
