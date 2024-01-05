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
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    fun mainActivity_onNavigationEvent_backScreen() {
        navigationController.backScreen(MainRoute.Team)
        navigationController
            .eventFlow
            .value
            .assertIsNull()
    }

    @Test
    fun mainActivity_onNavigationEvent_backScreenFromHome() {
        navigationController.backScreen(MainRoute.Home)
        Thread.sleep(1000)
        activity.state.assertIs(Lifecycle.State.DESTROYED)
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToHome() {
        navigationController.navigateToHome()
        navigationController
            .eventFlow
            .value
            .assertIsNull()
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToBoxScore() {
        navigationController.navigateToBoxScore(FinalGameId)
        navigationController
            .eventFlow
            .value
            .assertIsNull()
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToTeam() {
        navigationController.navigateToTeam(HomeTeamId)
        navigationController
            .eventFlow
            .value
            .assertIsNull()
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToPlayer() {
        navigationController.navigateToPlayer(HomePlayerId)
        navigationController
            .eventFlow
            .value
            .assertIsNull()
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToCalendar() {
        navigationController.navigateToCalendar(FinalGameTimeMs)
        navigationController
            .eventFlow
            .value
            .assertIsNull()
    }

    @Test
    fun mainActivity_onNavigationEvent_navigateToBet() {
        navigationController.navigateToBet(UserAccount)
        navigationController
            .eventFlow
            .value
            .assertIsNull()
    }

    @Test
    fun mainActivity_showErrorToast() {
        ToastEvent.OnError.send()
        toastEventManager
            .eventFlow
            .value
            .assertIsNull()
    }
}
