package com.jiachian.nbatoday.test.compose.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.compose.screen.home.HomeScreen
import com.jiachian.nbatoday.compose.screen.home.navigation.HomePage
import com.jiachian.nbatoday.testing.testtag.HomeTestTag
import com.jiachian.nbatoday.utils.assertCurrentRoute
import com.jiachian.nbatoday.utils.onAllNodesWithUnmergedTree
import org.junit.Test

class HomeScreenTest : BaseAndroidTest() {
    private lateinit var navController: TestNavHostController

    @Composable
    override fun provideComposable(): Any {
        val context = LocalContext.current
        navController = remember {
            TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
                enableOnBackPressed(false)
            }
        }
        HomeScreen(
            viewModel = HomeViewModel(
                composeViewModelProvider = composeViewModelProvider,
                navigationController = navigationController,
                dispatcherProvider = dispatcherProvider,
            ),
            navController = navController,
        )
        return super.provideComposable()
    }

    @Test
    fun homeScreen_navigateToSchedule() = inCompose {
        onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[0]
            .performClick()
        awaitIdle()
        navController.assertCurrentRoute(HomePage.SCHEDULE.route)
    }

    @Test
    fun homeScreen_navigateToStanding() = inCompose {
        onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[1]
            .performClick()
        awaitIdle()
        navController.assertCurrentRoute(HomePage.STANDING.route)
    }

    @Test
    fun homeScreen_navigateToUser() = inCompose {
        onAllNodesWithUnmergedTree(HomeTestTag.HomeBottomNavigation_BottomNavigationItem)[2]
            .performClick()
        awaitIdle()
        navController.assertCurrentRoute(HomePage.USER.route)
    }

    @Test
    fun homeScreen_defaultNavController() {
        composeTestRule.setContent {
            HomeScreen(
                viewModel = HomeViewModel(
                    composeViewModelProvider = composeViewModelProvider,
                    navigationController = navigationController,
                    dispatcherProvider = dispatcherProvider,
                )
            )
        }
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
