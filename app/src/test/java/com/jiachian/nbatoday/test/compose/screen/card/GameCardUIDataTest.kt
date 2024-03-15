package com.jiachian.nbatoday.test.compose.screen.card

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.UserToken
import com.jiachian.nbatoday.compose.screen.card.GameCardUIData
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsFalse
import com.jiachian.nbatoday.utils.assertIsTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class GameCardUIDataTest : BaseUnitTest() {
    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
    }

    @Test
    fun `login() expects user's information is correct`() {
        val data = getFinal()
        data.login(UserAccount, UserPassword)
        assertIs(dataHolder.user.value?.account, UserAccount)
        assertIs(dataHolder.user.value?.name, UserName)
        assertIs(dataHolder.user.value?.points, UserPoints)
        assertIs(dataHolder.user.value?.password, UserPassword)
        assertIs(dataHolder.user.value?.token, UserToken)
        assertIsTrue(dataHolder.user.value?.available)
    }

    @Test
    fun `register() expects user's information is correct`() {
        val data = getFinal()
        data.register(UserAccount, UserPassword)
        assertIs(dataHolder.user.value?.account, UserAccount)
        assertIs(dataHolder.user.value?.name, UserName)
        assertIs(dataHolder.user.value?.points, UserPoints)
        assertIs(dataHolder.user.value?.password, UserPassword)
        assertIs(dataHolder.user.value?.token, UserToken)
        assertIsTrue(dataHolder.user.value?.available)
    }

    @Test
    fun `bet() expects the bet is saved`() {
        val data = getFinal()
        data.login(UserAccount, UserPassword)
        data.bet(BetPoints, BetPoints)
        val betsAndGames = dataHolder.betsAndGames.stateIn(emptyList())
        assertIsTrue(dataHolder.bets.value.isNotEmpty())
        assertIsTrue(betsAndGames.value.any { it.game.gameId == FinalGameId })
    }

    @Test
    fun `setBetDialogVisible(visible) expects betDialogVisible is true`() {
        val data = getFinal()
        data.setBetDialogVisible(true)
        assertIsTrue(data.betDialogVisible.value)
    }

    @Test
    fun `setBetDialogVisible(invisible) expects betDialogVisible is false`() {
        val data = getFinal()
        data.setBetDialogVisible(false)
        assertIsFalse(data.betDialogVisible.value)
    }

    @Test
    fun `createBetDialogViewModel() with finalGame expects gameAndBets is correct`() {
        val data = getFinal()
        val dialogViewModel = data.createBetDialogViewModel()
        val expected = GameAndBetsGenerator.getFinal()
        assertIs(dialogViewModel.gameAndBets, expected)
    }

    @Test
    fun `createBetDialogViewModel() with playingGame expects gameAndBets is correct`() {
        val data = getPlaying()
        val dialogViewModel = data.createBetDialogViewModel()
        val expected = GameAndBetsGenerator.getPlaying()
        assertIs(dialogViewModel.gameAndBets, expected)
    }

    @Test
    fun `createBetDialogViewModel() with comingSoonGame expects gameAndBets is correct`() {
        val data = getComingSoon()
        val dialogViewModel = data.createBetDialogViewModel()
        val expected = GameAndBetsGenerator.getComingSoon()
        assertIs(dialogViewModel.gameAndBets, expected)
    }

    @Test
    fun `setCardExpanded(expanded) expects expanded is true`() {
        val data = getFinal()
        data.setCardExpanded(true)
        assertIsTrue(data.expanded.value)
    }

    @Test
    fun `setCardExpanded(unExpanded) expects expanded is false`() {
        val data = getFinal()
        data.setCardExpanded(false)
        assertIsFalse(data.expanded.value)
    }

    private fun getFinal(): GameCardUIData {
        return GameCardUIData(
            gameAndBets = GameAndBetsGenerator.getFinal(),
            betRepository = get(),
            userRepository = get(),
            dispatcherProvider = dispatcherProvider,
        )
    }

    private fun getPlaying(): GameCardUIData {
        return GameCardUIData(
            gameAndBets = GameAndBetsGenerator.getPlaying(),
            betRepository = get(),
            userRepository = get(),
            dispatcherProvider = dispatcherProvider,
        )
    }

    private fun getComingSoon(): GameCardUIData {
        return GameCardUIData(
            gameAndBets = GameAndBetsGenerator.getComingSoon(),
            betRepository = get(),
            userRepository = get(),
            dispatcherProvider = dispatcherProvider,
        )
    }
}
