package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.PlayerCareerFactory
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.utils.onNodeWithMergedTag
import com.jiachian.nbatoday.utils.onNodeWithTag
import kotlin.math.pow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerCareerScreenTest : BaseAndroidTest() {

    private lateinit var viewModel: PlayerInfoViewModel
    private lateinit var repository: TestRepository
    private var isBack = false
    private var currentState: NbaState? = null

    private val playerCareer = PlayerCareerFactory.createHomePlayerCareer()

    @Before
    fun setup() = runTest {
        repository = TestRepository()
        viewModel = PlayerInfoViewModel(
            playerId = playerCareer.personId,
            repository = repository,
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider
        )
        composeTestRule.setContent {
            PlayerCareerScreen(
                viewModel = viewModel,
                onBack = { isBack = true }
            )
        }
    }

    @After
    fun teardown() {
        repository.clear()
        isBack = false
        currentState = null
    }

    @Test
    fun player_clickBack_checksIsBack() {
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerScreen_Btn_Back")
            .performClick()
            .assertIsDisplayed()
        assertThat(isBack, `is`(true))
    }

    @Test
    fun player_checksPlayerInfo() {
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_PlayerInfo")
            .assertTextEquals(
                context.getString(
                    R.string.player_career_info,
                    playerCareer.info.team.location,
                    playerCareer.info.team.teamName,
                    playerCareer.info.jersey,
                    playerCareer.info.position
                )
            )
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_PlayerName")
            .assertTextEquals(playerCareer.info.playerName)
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Image_Greatest75")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_PPG")
            .assertTextEquals(playerCareer.info.headlineStats.points.toString())
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_RPG")
            .assertTextEquals(playerCareer.info.headlineStats.rebounds.toString())
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_APG")
            .assertTextEquals(playerCareer.info.headlineStats.assists.toString())
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_PIE")
            .assertTextEquals((playerCareer.info.headlineStats.impact * 100).decimalFormat())
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_Height")
            .assertTextEquals(
                context.getString(
                    R.string.player_career_info_height_value,
                    (playerCareer.info.height / 100).decimalFormat(2)
                )
            )
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_Height")
            .assertTextEquals(
                context.getString(
                    R.string.player_career_info_height_value,
                    (playerCareer.info.height / 100).decimalFormat(2)
                )
            )
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_Weight")
            .assertTextEquals(
                context.getString(
                    R.string.player_career_info_weight_value,
                    playerCareer.info.weight.decimalFormat()
                )
            )
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_Country")
            .assertTextEquals(playerCareer.info.country)
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_LastAttended")
            .assertTextEquals(playerCareer.info.school)
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_Age")
            .assertTextEquals(playerCareer.info.playerAge.toString())
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_Birth")
            .assertTextEquals(playerCareer.info.birthDate)
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_Draft")
            .assertTextEquals(
                context.getString(
                    R.string.player_career_info_draft_format,
                    playerCareer.info.draftYear,
                    playerCareer.info.draftRound,
                    playerCareer.info.draftNumber
                )
            )
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerInfo_Text_Experience")
            .assertTextEquals(playerCareer.info.seasonExperience.toString())
    }

    @Test
    fun player_checkPlayerStats() {
        val stats = playerCareer.stats.careerStats[0]
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerStats_LC_Year")
            .onNodeWithTag("PlayerCareerStats_Row_Year")
            .onNodeWithTag("PlayerCareerStats_Text_Year")
            .assertTextEquals(stats.timeFrame)
        composeTestRule
            .onNodeWithMergedTag("PlayerCareerStats_LC_Year")
            .onNodeWithTag("PlayerCareerStats_Row_Year")
            .onNodeWithTag("PlayerCareerStats_Text_TeamNaeAbbr")
            .assertTextEquals(stats.teamNameAbbr)
        val statsRoot = composeTestRule
            .onNodeWithMergedTag("PlayerCareerStats_LC_Stats")
            .onNodeWithTag("PlayerCareerStats_Row_Stats")
        statsRoot.apply {
            viewModel.statsLabels.value.forEachIndexed { index, label ->
                onNodeWithTag("PlayerCareerStats_Text_Stats", index)
                    .assertTextEquals(
                        when (label.text) {
                            "GP" -> stats.gamePlayed.toString()
                            "W" -> stats.win.toString()
                            "L" -> stats.lose.toString()
                            "WIN%" -> stats.winPercentage.decimalFormat()
                            "PTS" -> (stats.points.toDouble() / stats.gamePlayed).decimalFormat()
                            "FGM" -> (stats.fieldGoalsMade.toDouble() / stats.gamePlayed).decimalFormat()
                            "FGA" -> (stats.fieldGoalsAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                            "FG%" -> stats.fieldGoalsPercentage.decimalFormat()
                            "3PM" -> (stats.threePointersMade.toDouble() / stats.gamePlayed).decimalFormat()
                            "3PA" -> (stats.threePointersAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                            "3P%" -> stats.threePointersPercentage.decimalFormat()
                            "FTM" -> (stats.freeThrowsMade.toDouble() / stats.gamePlayed).decimalFormat()
                            "FTA" -> (stats.freeThrowsAttempted.toDouble() / stats.gamePlayed).decimalFormat()
                            "FT%" -> stats.freeThrowsPercentage.decimalFormat()
                            "OREB" -> (stats.reboundsOffensive.toDouble() / stats.gamePlayed).decimalFormat()
                            "DREB" -> (stats.reboundsDefensive.toDouble() / stats.gamePlayed).decimalFormat()
                            "REB" -> (stats.reboundsTotal.toDouble() / stats.gamePlayed).decimalFormat()
                            "AST" -> (stats.assists.toDouble() / stats.gamePlayed).decimalFormat()
                            "TOV" -> (stats.turnovers.toDouble() / stats.gamePlayed).decimalFormat()
                            "STL" -> (stats.steals.toDouble() / stats.gamePlayed).decimalFormat()
                            "BLK" -> (stats.blocks.toDouble() / stats.gamePlayed).decimalFormat()
                            "PF" -> (stats.foulsPersonal.toDouble() / stats.gamePlayed).decimalFormat()
                            "+/-" -> stats.plusMinus.toString()
                            else -> ""
                        }
                    )
            }
        }
    }

    private fun Double.decimalFormat(radix: Int = 1): String {
        val value = (this * 10.0.pow(radix)).toInt() / 10.0.pow(radix)
        return value.toString()
    }
}
