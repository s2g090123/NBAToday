package com.jiachian.nbatoday.compose.screen.score

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.models.BoxScoreFactory
import com.jiachian.nbatoday.models.NbaGameFactory
import com.jiachian.nbatoday.models.TestRepository
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.score.PlayerActiveStatus
import com.jiachian.nbatoday.utils.assertPopupExist
import com.jiachian.nbatoday.utils.getOrError
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
        val home = score.homeTeam.getOrError()
        val away = score.awayTeam.getOrError()
        composeTestRule
            .onNodeWithMergedTag("ScoreScreen_Text_Date")
            .assertTextEquals(score.gameDate)
        composeTestRule
            .onNodeWithMergedTag("ScoreTotal_Text_ScoreComparison")
            .assertTextEquals(
                context.getString(
                    R.string.box_score_comparison,
                    home.score,
                    away.score
                )
            )
        composeTestRule
            .onNodeWithMergedTag("ScoreTotal_Text_Status")
            .assertTextEquals(game.gameStatusText)
        composeTestRule
            .onNodeWithMergedTag("ScoreTotal_TeamInfo_Home")
            .onNodeWithTag("TeamInfo_Text_Name")
            .assertTextEquals(home.team.teamName)
        composeTestRule
            .onNodeWithMergedTag("ScoreTotal_TeamInfo_Away")
            .onNodeWithTag("TeamInfo_Text_Name")
            .assertTextEquals(away.team.teamName)
        val scorePeriod = composeTestRule
            .onNodeWithMergedTag("ScoreScreen_ScorePeriod")
        val homePeriod = scorePeriod
            .onNodeWithTag("ScorePeriod_Box_Score", 0)
        val awayPeriod = scorePeriod
            .onNodeWithTag("ScorePeriod_Box_Score", 1)
        homePeriod.apply {
            onNodeWithTag("ScorePeriod_Text_TeamName")
                .assertTextEquals(home.team.teamName)
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
                .assertTextEquals(away.team.teamName)
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
        val scoreLabel = LabelHelper.createScoreLabel()
        val home = score.homeTeam.getOrError()
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
                scoreLabel.forEachIndexed { index, label ->
                    onNodeWithTag("PlayerStatistics_Box_Label", index)
                        .performScrollTo()
                        .assertIsDisplayed()
                        .performClick()
                    composeTestRule
                        .assertPopupExist()
                        .onPopup()
                        .onNodeWithTag("LabelAboutPopup_Text_About")
                        .assertTextEquals(context.getString(label.infoRes))
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
                        scoreLabel.forEachIndexed { labelIndex, label ->
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
        val scoreLabel = LabelHelper.createScoreLabel()
        val away = score.awayTeam.getOrError()
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
                scoreLabel.forEachIndexed { index, label ->
                    onNodeWithTag("PlayerStatistics_Box_Label", index)
                        .performScrollTo()
                        .assertIsDisplayed()
                        .performClick()
                    composeTestRule
                        .assertPopupExist()
                        .onPopup()
                        .onNodeWithTag("LabelAboutPopup_Text_About")
                        .assertTextEquals(context.getString(label.infoRes))
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
                        scoreLabel.forEachIndexed { labelIndex, label ->
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
        val home = score.homeTeam.getOrError().statistics.getOrError()
        val away = score.awayTeam.getOrError().statistics.getOrError()
        composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Tab_TeamStats")
            .performClick()
        composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Pager")
            .onNodeWithTag("ScoreDetail_TeamStatistics")
            .apply {
                onNodeWithTag("TeamStatistics_TeamStatsRow", 0)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.points.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 0)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.points.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 1)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.fieldGoalsFormat)
                onNodeWithTag("TeamStatistics_TeamStatsRow", 1)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.fieldGoalsFormat)
                onNodeWithTag("TeamStatistics_TeamStatsRow", 2)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.twoPointsFormat)
                onNodeWithTag("TeamStatistics_TeamStatsRow", 2)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.twoPointsFormat)
                onNodeWithTag("TeamStatistics_TeamStatsRow", 3)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.threePointsFormat)
                onNodeWithTag("TeamStatistics_TeamStatsRow", 3)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.threePointsFormat)
                onNodeWithTag("TeamStatistics_TeamStatsRow", 4)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.freeThrowFormat)
                onNodeWithTag("TeamStatistics_TeamStatsRow", 4)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.freeThrowFormat)
                onNodeWithTag("TeamStatistics_TeamStatsRow", 5)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.reboundsPersonal.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 5)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.reboundsPersonal.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 6)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.reboundsDefensive.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 6)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.reboundsDefensive.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 7)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.reboundsOffensive.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 7)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.reboundsOffensive.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 8)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.assists.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 8)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.assists.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 9)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.blocks.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 9)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.blocks.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 10)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.steals.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 10)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.steals.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 11)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.turnovers.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 11)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.turnovers.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 12)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.pointsFastBreak.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 12)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.pointsFastBreak.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 13)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.pointsFromTurnovers.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 13)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.pointsFromTurnovers.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 14)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.pointsInThePaint.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 14)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.pointsInThePaint.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 15)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.pointsSecondChance.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 15)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.pointsSecondChance.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 16)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.benchPoints.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 16)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.benchPoints.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 17)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.foulsPersonal.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 17)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.foulsPersonal.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 18)
                    .onNodeWithTag("TeamStatsRow_Text_Home")
                    .assertTextEquals(home.foulsTechnical.toString())
                onNodeWithTag("TeamStatistics_TeamStatsRow", 18)
                    .onNodeWithTag("TeamStatsRow_Text_Away")
                    .assertTextEquals(away.foulsTechnical.toString())
            }
    }

    @Test
    fun boxScore_checksLeaderStatsUI() {
        val homeLeader = viewModel.homeLeader.value.getOrError()
        val awayLeader = viewModel.awayLeader.value.getOrError()
        val homeStats = homeLeader.statistics.getOrError()
        val awayStats = awayLeader.statistics.getOrError()
        composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Tab_LeaderStats")
            .performClick()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithMergedTag("ScoreDetail_Pager")
            .onNodeWithTag("ScoreDetail_LeaderStatistics")
            .apply {
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 0)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeLeader.nameAbbr)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 0)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayLeader.nameAbbr)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 1)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeLeader.position)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 1)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayLeader.position)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 2)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.minutes)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 2)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.minutes)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 3)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.points.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 3)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.points.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 4)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.plusMinusPoints.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 4)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.plusMinusPoints.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 5)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.fieldGoalsFormat)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 5)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.fieldGoalsFormat)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 6)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.twoPointsFormat)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 6)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.twoPointsFormat)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 7)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.threePointsFormat)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 7)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.threePointsFormat)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 8)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.freeThrowFormat)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 8)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.freeThrowFormat)
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 9)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.reboundsTotal.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 9)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.reboundsTotal.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 10)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.reboundsDefensive.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 10)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.reboundsDefensive.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 11)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.reboundsOffensive.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 11)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.reboundsOffensive.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 12)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.assists.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 12)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.assists.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 13)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.blocks.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 13)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.blocks.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 14)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.steals.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 14)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.steals.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 15)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.turnovers.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 15)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.turnovers.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 16)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.foulsPersonal.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 16)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.foulsPersonal.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 17)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Home")
                    .assertTextEquals(homeStats.foulsTechnical.toString())
                onNodeWithTag("LeaderStatistics_LeaderStatisticsRow", 17)
                    .onNodeWithTag("LeaderStatisticsRow_Text_Away")
                    .assertTextEquals(awayStats.foulsTechnical.toString())
            }
    }

    private fun getPlayerStats(
        label: ScoreLabel,
        player: BoxScore.BoxScoreTeam.Player
    ): String {
        val statistics = player.statistics.getOrError()
        return when (label.textRes) {
            R.string.label_min -> statistics.minutes
            R.string.label_fgm -> statistics.fieldGoalProportion
            R.string.label_3pm -> statistics.threePointProportion
            R.string.label_ftm -> statistics.freeThrowProportion
            R.string.label_plus_minus -> statistics.plusMinusPoints.toString()
            R.string.label_or -> statistics.reboundsOffensive.toString()
            R.string.label_dr -> statistics.reboundsDefensive.toString()
            R.string.label_tr -> statistics.reboundsTotal.toString()
            R.string.label_as -> statistics.assists.toString()
            R.string.label_pf -> statistics.foulsPersonal.toString()
            R.string.label_st -> statistics.steals.toString()
            R.string.label_to -> statistics.turnovers.toString()
            R.string.label_bs -> statistics.blocks.toString()
            R.string.label_ba -> statistics.blocksReceived.toString()
            R.string.label_pts -> statistics.points.toString()
            R.string.label_eff -> statistics.efficiency.toString()
            else -> ""
        }
    }
}
