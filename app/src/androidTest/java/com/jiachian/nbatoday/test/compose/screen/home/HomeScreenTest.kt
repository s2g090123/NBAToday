package com.jiachian.nbatoday.test.compose.screen.home

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.home.main.ui.HomeScreen
import com.jiachian.nbatoday.home.main.ui.navigation.HomeRoute
import com.jiachian.nbatoday.navigation.TestNavigationController
import com.jiachian.nbatoday.testing.testtag.HomeTestTag
import com.jiachian.nbatoday.utils.assertCurrentRoute
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import org.junit.Before
import org.junit.Test

class HomeScreenTest : BaseAndroidTest() {
    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        composeTestRule.setContent {
            HomeScreen(
                navigationController = TestNavigationController(),
                navController = TestNavHostController(LocalContext.current).apply {
                    navigatorProvider.addNavigator(ComposeNavigator())
                    enableOnBackPressed(false)
                    navController = this
                },
            )
        }
    }

    @Test
    fun homeScreen_navigateToSchedule() {
        composeTestRule.apply {
            onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[0]
                .performClick()
            navController.assertCurrentRoute(HomeRoute.SCHEDULE.route)
        }
    }

    @Test
    fun homeScreen_navigateToStanding() {
        composeTestRule.apply {
            onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[1]
                .performClick()
            navController.assertCurrentRoute(HomeRoute.STANDING.route)
        }
    }

    @Test
    fun homeScreen_navigateToUser() {
        composeTestRule.apply {
            onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[2]
                .performClick()
            navController.assertCurrentRoute(HomeRoute.USER.route)
        }
    }

    @Test
    fun homeScreen_defaultNavController() {
        composeTestRule.apply {
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
}
