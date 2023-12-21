package com.jiachian.nbatoday.test.compose.screen.card

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.UserToken
import com.jiachian.nbatoday.compose.screen.card.GameCardViewModel
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsFalse
import com.jiachian.nbatoday.utils.assertIsTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameCardViewModelTest : BaseUnitTest() {
    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
    }

    @Test
    fun `login() expects user's information is correct`() {
        val viewModel = getFinalViewModel()
        viewModel.login(UserAccount, UserPassword)
        assertIs(dataHolder.user.value?.account, UserAccount)
        assertIs(dataHolder.user.value?.name, UserName)
        assertIs(dataHolder.user.value?.points, UserPoints)
        assertIs(dataHolder.user.value?.password, UserPassword)
        assertIs(dataHolder.user.value?.token, UserToken)
        assertIsTrue(dataHolder.user.value?.available)
    }

    @Test
    fun `register() expects user's information is correct`() {
        val viewModel = getFinalViewModel()
        viewModel.register(UserAccount, UserPassword)
        assertIs(dataHolder.user.value?.account, UserAccount)
        assertIs(dataHolder.user.value?.name, UserName)
        assertIs(dataHolder.user.value?.points, UserPoints)
        assertIs(dataHolder.user.value?.password, UserPassword)
        assertIs(dataHolder.user.value?.token, UserToken)
        assertIsTrue(dataHolder.user.value?.available)
    }

    @Test
    fun `bet() expects the bet is saved`() {
        val viewModel = getFinalViewModel()
        viewModel.login(UserAccount, UserPassword)
        viewModel.bet(BasicNumber.toLong(), BasicNumber.toLong())
        val betsAndGames = dataHolder.betsAndGames.stateIn(emptyList())
        assertIsTrue(dataHolder.bets.value.isNotEmpty())
        assertIsTrue(betsAndGames.value?.any { it.game.gameId == FinalGameId })
    }

    @Test
    fun `setBetDialogVisible(visible) expects betDialogVisible is true`() {
        val viewModel = getFinalViewModel()
        viewModel.setBetDialogVisible(true)
        assertIsTrue(viewModel.betDialogVisible.value)
    }

    @Test
    fun `setBetDialogVisible(invisible) expects betDialogVisible is false`() {
        val viewModel = getFinalViewModel()
        viewModel.setBetDialogVisible(false)
        assertIsFalse(viewModel.betDialogVisible.value)
    }

    @Test
    fun `createBetDialogViewModel() with finalGame expects gameAndBets is correct`() {
        val viewModel = getFinalViewModel()
        val dialogViewModel = viewModel.createBetDialogViewModel()
        val expected = GameAndBetsGenerator.getFinal()
        assertIs(dialogViewModel.gameAndBets, expected)
    }

    @Test
    fun `createBetDialogViewModel() with playingGame expects gameAndBets is correct`() {
        val viewModel = getPlayingViewModel()
        val dialogViewModel = viewModel.createBetDialogViewModel()
        val expected = GameAndBetsGenerator.getPlaying()
        assertIs(dialogViewModel.gameAndBets, expected)
    }

    @Test
    fun `createBetDialogViewModel() with comingSoonGame expects gameAndBets is correct`() {
        val viewModel = getComingSoonViewModel()
        val dialogViewModel = viewModel.createBetDialogViewModel()
        val expected = GameAndBetsGenerator.getComingSoon()
        assertIs(dialogViewModel.gameAndBets, expected)
    }

    @Test
    fun `setCardExpanded(expanded) expects expanded is true`() {
        val viewModel = getFinalViewModel()
        viewModel.setCardExpanded(true)
        assertIsTrue(viewModel.expanded.value)
    }

    @Test
    fun `setCardExpanded(unExpanded) expects expanded is false`() {
        val viewModel = getFinalViewModel()
        viewModel.setCardExpanded(false)
        assertIsFalse(viewModel.expanded.value)
    }

    private fun getFinalViewModel(): GameCardViewModel {
        return composeViewModelProvider.getGameCardViewModel(
            gameAndBets = GameAndBetsGenerator.getFinal(),
            dispatcherProvider = dispatcherProvider,
        )
    }

    private fun getPlayingViewModel(): GameCardViewModel {
        return composeViewModelProvider.getGameCardViewModel(
            gameAndBets = GameAndBetsGenerator.getPlaying(),
            dispatcherProvider = dispatcherProvider,
        )
    }

    private fun getComingSoonViewModel(): GameCardViewModel {
        return composeViewModelProvider.getGameCardViewModel(
            gameAndBets = GameAndBetsGenerator.getComingSoon(),
            dispatcherProvider = dispatcherProvider,
        )
    }
}
