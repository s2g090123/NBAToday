package com.jiachian.nbatoday.test.compose.screen.player

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.local.PlayerGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.navigation.TestNavigationController
import com.jiachian.nbatoday.player.ui.PlayerScreen
import com.jiachian.nbatoday.player.ui.PlayerViewModel
import com.jiachian.nbatoday.player.ui.model.PlayerStatsLabel
import com.jiachian.nbatoday.player.ui.model.PlayerTableLabel
import com.jiachian.nbatoday.testing.testtag.PlayerTestTag
import com.jiachian.nbatoday.utils.LabelHelper
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import com.jiachian.nbatoday.utils.waitUntilExists
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerScreenTest : BaseAndroidTest() {
    private lateinit var navigationController: TestNavigationController
    private lateinit var viewModel: PlayerViewModel

    @Before
    fun setup() = runTest {
        viewModel = PlayerViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.Player.param to "$HomePlayerId")),
            playerUseCase = useCaseProvider.player,
            dispatcherProvider = dispatcherProvider,
        )
        repositoryProvider.team.addTeams()
        repositoryProvider.team.updateTeamPlayers(HomeTeamId)
        composeTestRule.setContent {
            PlayerScreen(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                navigationController = TestNavigationController().apply {
                    navigationController = this
                },
            )
        }
    }

    @Test
    fun checksInfo() {
        val player = PlayerGenerator.getHome()
        composeTestRule.apply {
            waitUntilExists(hasTestTag(PlayerTestTag.PlayerTitle_Text_Detail))
            onNodeWithUnmergedTree(PlayerTestTag.PlayerTitle_Text_Detail)
                .assertTextEquals(player.info.detail)
            onNodeWithUnmergedTree(PlayerTestTag.PlayerTitle_Text_Name)
                .assertTextEquals(player.info.playerName)
            onNodeWithUnmergedTree(PlayerTestTag.PlayerTitle_Image_Greatest)
                .assertIsDisplayed()
            PlayerTableLabel.values().forEachIndexed { index, label ->
                val value = LabelHelper.getValueByLabel(label, player.info)
                onAllNodesWithUnmergedTree(PlayerTestTag.PlayerInfoBox_Text_Value)[index]
                    .assertTextEquals(value)
            }
        }
    }

    @Test
    fun checksStats() {
        val player = PlayerGenerator.getHome()
        composeTestRule.apply {
            waitUntilExists(hasTestTag(PlayerTestTag.PlayerStatsYearText_Text_Time))
            onNodeWithUnmergedTree(PlayerTestTag.PlayerStatsYearText_Text_Time)
                .assertTextEquals(player.stats.stats.first().timeFrame)
            onNodeWithUnmergedTree(PlayerTestTag.PlayerStatsYearText_Text_TeamName)
                .assertTextEquals(player.info.team.abbreviation)
            PlayerStatsLabel.values().forEachIndexed { index, label ->
                val value = LabelHelper.getValueByLabel(label, player.stats.stats.first())
                onAllNodesWithUnmergedTree(PlayerTestTag.PlayerStatsText_Text)[index]
                    .assertTextEquals(value)
            }
        }
    }

    @Test
    fun back_backScreen() {
        composeTestRule.apply {
            onNodeWithUnmergedTree(PlayerTestTag.PlayerScreen_Button_Back)
                .performClick()
            navigationController.back.assertIsTrue()
        }
    }
}
