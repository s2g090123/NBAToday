package com.jiachian.nbatoday.test

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.FinalGameTimeMs
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.MainActivity
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.event.ToastEvent
import com.jiachian.nbatoday.event.send
import com.jiachian.nbatoday.event.toastEventManager
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsA
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityTest : BaseAndroidTest() {
    private lateinit var activity: ActivityScenario<MainActivity>

    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    @Before
    fun setup() {
        activity = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun teardown() {
        activity.close()
    }

    @Test
    fun mainActivity_onNavigationEvent_backScreen() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.backScreen(MainRoute.Team)
        event
            .await()
            .assertIsA(NavigationController.Event.BackScreen::class.java)
    }

    @Test
    fun mainActivity_onNavigationEvent_backScreenFromHome() {
        navigationController.backScreen(MainRoute.Home)
        Thread.sleep(1000)
        activity.state.assertIs(Lifecycle.State.DESTROYED)
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToHome() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToHome()
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToHome::class.java)
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToBoxScore() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToBoxScore(FinalGameId)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToBoxScore::class.java)
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToTeam() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToTeam(HomeTeamId)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToTeam::class.java)
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToPlayer() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToPlayer(HomePlayerId)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToPlayer::class.java)
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToCalendar() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToCalendar(FinalGameTimeMs)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToCalendar::class.java)
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToBet() = launch {
        val event = navigationController.eventFlow.defer(this)
        navigationController.navigateToBet(UserAccount)
        event
            .await()
            .assertIsA(NavigationController.Event.NavigateToBet::class.java)
    }

    @Test
    fun mainActivity_showErrorToast() = launch {
        val event = toastEventManager.eventFlow.defer(this)
        ToastEvent.OnError.send()
        delay(100)
        event
            .await()
            .assertIsA(ToastEvent.OnError::class.java)
    }
}
