package com.jiachian.nbatoday.compose.screen.player

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.models.PlayerCareerFactory
import com.jiachian.nbatoday.models.TestRepository
import com.jiachian.nbatoday.utils.onAllNodesWithMergedTag
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

    private lateinit var viewModel: PlayerViewModel
    private lateinit var repository: TestRepository
    private var isBack = false
    private var currentState: NbaState? = null

    private val playerCareer = PlayerCareerFactory.createHomePlayerCareer()

    @Before
    fun setup() = runTest {
        repository = TestRepository()
        viewModel = PlayerViewModel(
            playerId = playerCareer.playerId,
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
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[0]
            .assertTextEquals(playerCareer.info.points.toString())
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[1]
            .assertTextEquals(playerCareer.info.rebounds.toString())
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[2]
            .assertTextEquals(playerCareer.info.assists.toString())
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[3]
            .assertTextEquals(playerCareer.info.impact.toString())
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[4]
            .assertTextEquals(
                context.getString(
                    R.string.player_career_info_height_value,
                    playerCareer.info.heightFormat.toString()
                )
            )
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[5]
            .assertTextEquals(
                context.getString(
                    R.string.player_career_info_weight_value,
                    playerCareer.info.weight.decimalFormat()
                )
            )
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[6]
            .assertTextEquals(playerCareer.info.country)
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[7]
            .assertTextEquals(playerCareer.info.school)
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[8]
            .assertTextEquals(playerCareer.info.playerAge.toString())
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[9]
            .assertTextEquals(playerCareer.info.birthDate)
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[10]
            .assertTextEquals(
                context.getString(
                    R.string.player_career_info_draft_format,
                    playerCareer.info.draftYear,
                    playerCareer.info.draftRound,
                    playerCareer.info.draftNumber
                )
            )
        composeTestRule
            .onAllNodesWithMergedTag("PlayerInfoBox_Text_Value")[11]
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
