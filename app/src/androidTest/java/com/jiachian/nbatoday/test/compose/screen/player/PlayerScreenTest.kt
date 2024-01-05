package com.jiachian.nbatoday.test.compose.screen.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.HomePlayerFirstName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamLocation
import com.jiachian.nbatoday.HomeTeamName
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.player.PlayerScreen
import com.jiachian.nbatoday.compose.screen.player.PlayerViewModel
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsLabel
import com.jiachian.nbatoday.compose.screen.player.models.PlayerTableLabel
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.data.local.PlayerGenerator
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.PlayerTestTag
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkObject
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Test

class PlayerScreenTest : BaseAndroidTest() {
    private lateinit var viewModel: PlayerViewModel

    @Before
    fun setup() {
        viewModel = spyk(
            PlayerViewModel(
                playerId = HomePlayerId,
                repository = repositoryProvider.player,
                navigationController = navigationController,
                dispatcherProvider = dispatcherProvider,
            )
        )
    }

    @After
    fun teardown() {
        unmockkObject(viewModel)
    }

    @Composable
    override fun provideComposable(): Any {
        PlayerScreen(
            viewModel = viewModel
        )
        return super.provideComposable()
    }

    @Test
    fun playerScreen_checksPlayerInfoUI() = inCompose {
        onNodeWithUnmergedTree(PlayerTestTag.PlayerTitle_Text_Detail)
            .assertTextEquals("$HomeTeamLocation $HomeTeamName | #$BasicNumber | $BasicPosition")
        onNodeWithUnmergedTree(PlayerTestTag.PlayerTitle_Text_Name)
            .assertTextEquals(HomePlayerFirstName)
        onNodeWithUnmergedTree(PlayerTestTag.PlayerTitle_Image_Greatest)
            .assertIsDisplayed()
        PlayerTableLabel.values().forEachIndexed { index, label ->
            val value = LabelHelper.getValueByLabel(label, PlayerGenerator.getHome().info)
            onAllNodesWithUnmergedTree(PlayerTestTag.PlayerInfoBox_Text_Value)[index]
                .assertTextEquals(value)
        }
    }

    @Test
    fun playerScreen_checksPlayerStatsUI() = inCompose {
        onNodeWithUnmergedTree(PlayerTestTag.PlayerStatsYearText_Text_Time)
            .assertTextEquals("$BasicNumber")
        onNodeWithUnmergedTree(PlayerTestTag.PlayerStatsYearText_Text_TeamName)
            .assertTextEquals(HomeTeamAbbr)
        PlayerStatsLabel.values().forEachIndexed { index, label ->
            val value = LabelHelper.getValueByLabel(label, PlayerGenerator.getHome().stats.stats[0])
            onAllNodesWithUnmergedTree(PlayerTestTag.PlayerStatsText_Text)[index]
                .assertTextEquals(value)
        }
    }

    @Test
    fun playerScreen_checksLoading() {
        every {
            viewModel.playerUIState
        } returns MutableStateFlow(UIState.Loading())
        inCompose {
            onNodeWithUnmergedTree(PlayerTestTag.PlayerScreen_Loading)
                .assertIsDisplayed()
        }
    }

    @Test
    fun playerScreen_checksNotFound() {
        every {
            viewModel.playerUIState
        } returns MutableStateFlow(UIState.Loaded(null))
        inCompose {
            onNodeWithUnmergedTree(PlayerTestTag.PlayerScreen_PlayerNotFound)
                .assertIsDisplayed()
        }
    }

    @Test
    fun playerScreen_clicksBack() = inCompose {
        onNodeWithUnmergedTree(PlayerTestTag.PlayerScreen_Button_Back)
            .performClick()
        navigationController
            .eventFlow
            .value
            .assertIsA(NavigationController.Event.BackScreen::class.java)
            ?.departure
            .assertIs(MainRoute.Player)
    }
}
