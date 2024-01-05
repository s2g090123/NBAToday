package com.jiachian.nbatoday.test.compose.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.FinalGameTimeMs
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.MainViewModel
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.compose.screen.main.MainScreen
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.utils.assertCurrentRoute
import com.jiachian.nbatoday.utils.assertIsTrue
import org.junit.Test

class MainScreenTest : BaseAndroidTest() {
    private lateinit var navController: TestNavHostController

    @Composable
    override fun provideComposable(): Any {
        navController = TestNavHostController(LocalContext.current).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }
        MainScreen(
            viewModel = MainViewModel(
                repositoryProvider = repositoryProvider,
                dataStore = dataStore,
                navigationController = navigationController,
                viewModelProvider = composeViewModelProvider,
                dispatcherProvider = dispatcherProvider,
            ),
            navController = navController
        )
        return super.provideComposable()
    }

    @Test
    fun mainScreen_checksInitialState() = inCompose {
        navController.assertCurrentRoute(MainRoute.Splash.route)
    }

    @Test
    fun mainScreen_navigateToHome() = inCompose {
        runOnUiThread {
            navController.navigate(MainRoute.Home.route)
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.Home.route)
        composeViewModelProvider
            .getViewModelMap()
            .assertIsTrue { it.contains(MainRoute.Home) }
    }

    @Test
    fun mainScreen_navigateToBoxScore() = inCompose {
        runOnUiThread {
            navController.navigate("${MainRoute.BoxScore.path}/$FinalGameId")
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.BoxScore.route)
        composeViewModelProvider
            .getViewModelMap()
            .assertIsTrue { it.contains(MainRoute.BoxScore) }
    }

    @Test
    fun mainScreen_navigateToTeam() = inCompose {
        runOnUiThread {
            navController.navigate("${MainRoute.Team.path}/$HomeTeamId")
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.Team.route)
        composeViewModelProvider
            .getViewModelMap()
            .assertIsTrue { it.contains(MainRoute.Team) }
    }

    @Test
    fun mainScreen_navigateToPlayer() = inCompose {
        runOnUiThread {
            navController.navigate("${MainRoute.Player.path}/$HomePlayerId")
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.Player.route)
        composeViewModelProvider
            .getViewModelMap()
            .assertIsTrue { it.contains(MainRoute.Player) }
    }

    @Test
    fun mainScreen_navigateToCalendar() = inCompose {
        runOnUiThread {
            navController.navigate("${MainRoute.Calendar.path}/$FinalGameTimeMs")
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.Calendar.route)
        composeViewModelProvider
            .getViewModelMap()
            .assertIsTrue { it.contains(MainRoute.Calendar) }
    }

    @Test
    fun mainScreen_navigateToBet() = inCompose {
        runOnUiThread {
            navController.navigate("${MainRoute.Bet.path}/$UserAccount")
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.Bet.route)
        composeViewModelProvider
            .getViewModelMap()
            .assertIsTrue { it.contains(MainRoute.Bet) }
    }
}
