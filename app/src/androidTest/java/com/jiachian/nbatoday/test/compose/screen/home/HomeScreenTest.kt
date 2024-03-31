package com.jiachian.nbatoday.test.compose.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.home.main.ui.HomeScreen
import com.jiachian.nbatoday.home.main.ui.navigation.HomeRoute
import com.jiachian.nbatoday.home.schedule.ui.model.DateData
import com.jiachian.nbatoday.testing.testtag.HomeTestTag
import com.jiachian.nbatoday.utils.assertCurrentRoute
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import org.junit.Before
import org.junit.Test

class HomeScreenTest : BaseAndroidTest() {
    private lateinit var navController: TestNavHostController

    private var navigateToGame: String? = null
    private var navigateToTeam: Int? = null
    private var navigateToCalendar: DateData? = null
    private var navigateToBet: String? = null

    @Composable
    override fun ProvideComposable() {
        val context = LocalContext.current
        navController = remember {
            TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
                enableOnBackPressed(false)
            }
        }
        HomeScreen(
            navController = navController,
            navigateToBoxScore = {
                navigateToGame = it
            },
            navigateToTeam = {
                navigateToTeam = it
            },
            navigateToCalendar = {
                navigateToCalendar = it
            },
            navigateToBet = {
                navigateToBet = it
            },
        )
    }

    @Before
    fun setup() {
        navigateToGame = null
        navigateToTeam = null
        navigateToCalendar = null
        navigateToBet = null
    }

    @Test
    fun homeScreen_navigateToSchedule() = inCompose {
        onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[0]
            .performClick()
        awaitIdle()
        navController.assertCurrentRoute(HomeRoute.SCHEDULE.route)
    }

    @Test
    fun homeScreen_navigateToStanding() = inCompose {
        onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[1]
            .performClick()
        awaitIdle()
        navController.assertCurrentRoute(HomeRoute.STANDING.route)
    }

    @Test
    fun homeScreen_navigateToUser() = inCompose {
        onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[2]
            .performClick()
        awaitIdle()
        navController.assertCurrentRoute(HomeRoute.USER.route)
    }

    @Test
    fun homeScreen_defaultNavController() = inCompose {
        onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[0]
            .performClick()
            .assertIsSelected()
        onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[1]
            .performClick()
            .assertIsSelected()
        onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[2]
            .performClick()
            .assertIsSelected()
    }
}
