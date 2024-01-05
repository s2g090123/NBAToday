package com.jiachian.nbatoday.test.navigation

import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.FinalGameTimeMs
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import org.junit.Before
import org.junit.Test

class NavigationControllerTest {
    private lateinit var navigationController: NavigationController

    @Before
    fun setup() {
        navigationController = NavigationController()
    }

    @Test
    fun `navigateToHome() expects the event is correct`() {
        navigationController.navigateToHome()
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToHome::class.java
        )
    }

    @Test
    fun `navigateToBoxScore() expects the event is correct`() {
        navigationController.navigateToBoxScore(FinalGameId)
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToBoxScore::class.java
        )
        val event =
            navigationController.eventFlow.value as? NavigationController.Event.NavigateToBoxScore
        val gameId = event?.gameId
        assertIs(gameId, FinalGameId)
    }

    @Test
    fun `navigateToTeam() expects the event is correct`() {
        navigationController.navigateToTeam(HomeTeamId)
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToTeam::class.java
        )
        val event =
            navigationController.eventFlow.value as? NavigationController.Event.NavigateToTeam
        val teamId = event?.teamId
        assertIs(teamId, HomeTeamId)
    }

    @Test
    fun `navigateToPlayer() expects the event is correct`() {
        navigationController.navigateToPlayer(HomePlayerId)
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToPlayer::class.java
        )
        val event =
            navigationController.eventFlow.value as? NavigationController.Event.NavigateToPlayer
        val playerId = event?.playerId
        assertIs(playerId, HomePlayerId)
    }

    @Test
    fun `navigateToCalendar() expects the event is correct`() {
        navigationController.navigateToCalendar(FinalGameTimeMs)
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToCalendar::class.java
        )
        val event =
            navigationController.eventFlow.value as? NavigationController.Event.NavigateToCalendar
        val dateTime = event?.dateTime
        assertIs(dateTime, FinalGameTimeMs)
    }

    @Test
    fun `navigateToBet() expects the event is correct`() {
        navigationController.navigateToBet(UserAccount)
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.NavigateToBet::class.java
        )
        val event =
            navigationController.eventFlow.value as? NavigationController.Event.NavigateToBet
        val account = event?.account
        assertIs(account, UserAccount)
    }

    @Test
    fun `backScreen() expects the event is correct`() {
        navigationController.backScreen(MainRoute.Home)
        assertIsA(
            navigationController.eventFlow.value,
            NavigationController.Event.BackScreen::class.java
        )
        val event =
            navigationController.eventFlow.value as? NavigationController.Event.BackScreen
        val route = event?.departure
        assertIs(route, MainRoute.Home)
    }
}
