package com.jiachian.nbatoday.test.navigation

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.FinalGameTimeMs
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NavigationControllerTest : BaseUnitTest() {
    @Test
    fun `navigateToHome() expects the event is correct`() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToHome()
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToHome::class.java)
    }

    @Test
    fun `navigateToBoxScore() expects the event is correct`() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToBoxScore(FinalGameId)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToBoxScore::class.java)
            .gameId
            .assertIs(FinalGameId)
    }

    @Test
    fun `navigateToTeam() expects the event is correct`() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToTeam(HomeTeamId)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToTeam::class.java)
            .teamId
            .assertIs(HomeTeamId)
    }

    @Test
    fun `navigateToPlayer() expects the event is correct`() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToPlayer(HomePlayerId)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToPlayer::class.java)
            .playerId
            .assertIs(HomePlayerId)
    }

    @Test
    fun `navigateToCalendar() expects the event is correct`() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToCalendar(FinalGameTimeMs)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToCalendar::class.java)
            .dateTime
            .assertIs(FinalGameTimeMs)
    }

    @Test
    fun `navigateToBet() expects the event is correct`() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToBet(UserAccount)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToBet::class.java)
            .account
            .assertIs(UserAccount)
    }

    @Test
    fun `backScreen() expects the event is correct`() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.backScreen(MainRoute.Home)
        event
            .await()
            .assertIsA(NavigationController.Event.BackScreen::class.java)
            .departure
            .assertIs(MainRoute.Home)
    }
}
