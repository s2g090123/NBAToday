package com.jiachian.nbatoday.test.utils

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.FinalGameTimeMs
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.calendar.CalendarViewModel
import com.jiachian.nbatoday.compose.screen.card.GameCardUIData
import com.jiachian.nbatoday.compose.screen.home.schedule.SchedulePageViewModel
import com.jiachian.nbatoday.compose.screen.home.standing.StandingPageViewModel
import com.jiachian.nbatoday.compose.screen.home.user.UserPageViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import com.jiachian.nbatoday.utils.assertIsFalse
import com.jiachian.nbatoday.utils.assertIsTrue
import org.junit.Before
import org.junit.Test

class ComposeViewModelProviderTest : BaseUnitTest() {
    private lateinit var viewModelProvider: ComposeViewModelProvider

    @Before
    fun setup() {
        viewModelProvider = ComposeViewModelProvider(
            repositoryProvider = repositoryProvider,
            dataStore = dataStore,
            navigationController = navigationController
        )
    }

    @Test
    fun `getHomeViewModel() expects correct`() {
        viewModelProvider
            .getHomeViewModel(dispatcherProvider = dispatcherProvider)
            .assertIsA(HomeViewModel::class.java)
    }

    @Test
    fun `getHomeViewModel() expects existing one`() {
        val viewModel = viewModelProvider.getHomeViewModel(dispatcherProvider)
        viewModelProvider
            .getHomeViewModel(dispatcherProvider)
            .assertIs(viewModel)
    }

    @Test
    fun `getBoxScoreViewModel() expects correct`() {
        viewModelProvider
            .getBoxScoreViewModel(
                gameId = FinalGameId,
                dispatcherProvider = dispatcherProvider
            )
            .assertIsA(BoxScoreViewModel::class.java)
    }

    @Test
    fun `getBoxScoreViewModel() expects existing one`() {
        val viewModel = viewModelProvider.getBoxScoreViewModel(FinalGameId, dispatcherProvider)
        viewModelProvider
            .getBoxScoreViewModel(FinalGameId, dispatcherProvider)
            .assertIs(viewModel)
    }

    @Test
    fun `getTeamViewModel() expects correct`() {
        viewModelProvider
            .getTeamViewModel(
                teamId = HomeTeamId,
                dispatcherProvider = dispatcherProvider
            )
            .assertIsA(TeamViewModel::class.java)
    }

    @Test
    fun `getTeamViewModel() expects existing one`() {
        val viewModel = viewModelProvider.getTeamViewModel(HomeTeamId, dispatcherProvider)
        viewModelProvider
            .getTeamViewModel(HomeTeamId, dispatcherProvider)
            .assertIs(viewModel)
    }

    @Test
    fun `getPlayerViewModel() expects correct`() {
        viewModelProvider
            .getPlayerViewModel(
                playerId = HomePlayerId,
                dispatcherProvider = dispatcherProvider
            )
            .assertIsA(PlayerViewModel::class.java)
    }

    @Test
    fun `getPlayerViewModel() expects existing one`() {
        val viewModel = viewModelProvider.getPlayerViewModel(HomePlayerId, dispatcherProvider)
        viewModelProvider
            .getPlayerViewModel(HomePlayerId, dispatcherProvider)
            .assertIs(viewModel)
    }

    @Test
    fun `getCalendarViewModel() expects correct`() {
        viewModelProvider
            .getCalendarViewModel(
                dateTime = FinalGameTimeMs,
                dispatcherProvider = dispatcherProvider
            )
            .assertIsA(CalendarViewModel::class.java)
    }

    @Test
    fun `getCalendarViewModel() expects existing one`() {
        val viewModel = viewModelProvider.getCalendarViewModel(FinalGameTimeMs, dispatcherProvider)
        viewModelProvider
            .getCalendarViewModel(FinalGameTimeMs, dispatcherProvider)
            .assertIs(viewModel)
    }

    @Test
    fun `getBetViewModel() expects correct`() {
        viewModelProvider
            .getBetViewModel(
                account = UserAccount,
                dispatcherProvider = dispatcherProvider
            )
            .assertIsA(BetViewModel::class.java)
    }

    @Test
    fun `getBetViewModel() expects existing one`() {
        val viewModel = viewModelProvider.getBetViewModel(UserAccount, dispatcherProvider)
        viewModelProvider
            .getBetViewModel(UserAccount, dispatcherProvider)
            .assertIs(viewModel)
    }

    @Test
    fun `getSchedulePageViewModel() expects correct`() {
        viewModelProvider
            .getSchedulePageViewModel(dispatcherProvider = dispatcherProvider)
            .assertIsA(SchedulePageViewModel::class.java)
    }

    @Test
    fun `getStandingPageViewModel() expects correct`() {
        viewModelProvider
            .getStandingPageViewModel(dispatcherProvider = dispatcherProvider)
            .assertIsA(StandingPageViewModel::class.java)
    }

    @Test
    fun `getUserPageViewModel() expects correct`() {
        viewModelProvider
            .getUserPageViewModel(dispatcherProvider = dispatcherProvider)
            .assertIsA(UserPageViewModel::class.java)
    }

    @Test
    fun `getGameCardViewModel() expects correct`() {
        viewModelProvider
            .getGameCardViewModel(
                gameAndBets = GameAndBetsGenerator.getFinal(),
                dispatcherProvider = dispatcherProvider
            )
            .assertIsA(GameCardUIData::class.java)
    }

    @Test
    fun `removeViewModel() expects viewModel is removed`() {
        viewModelProvider.getHomeViewModel(dispatcherProvider)
        viewModelProvider
            .getViewModelMap()
            .assertIsTrue { it.contains(MainRoute.Home) }
        viewModelProvider.removeViewModel(MainRoute.Home)
        viewModelProvider
            .getViewModelMap()
            .assertIsFalse { it.contains(MainRoute.Home) }
    }
}
