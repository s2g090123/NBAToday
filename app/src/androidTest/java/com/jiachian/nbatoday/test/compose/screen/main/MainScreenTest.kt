package com.jiachian.nbatoday.test.compose.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.FinalGameTimeMs
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.compose.screen.main.MainScreen
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.utils.assertCurrentRoute
import org.junit.Test

class MainScreenTest : BaseAndroidTest() {
    private lateinit var navController: TestNavHostController

    @Composable
    override fun ProvideComposable() {
        val context = LocalContext.current
        navController = remember {
            TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
        }
        MainScreen(
            navController = navController
        )
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
    }

    @Test
    fun mainScreen_navigateToBoxScore() = inCompose {
        runOnUiThread {
            navController.navigate("${MainRoute.BoxScore.path}/$FinalGameId")
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.BoxScore.route)
    }

    @Test
    fun mainScreen_navigateToTeam() = inCompose {
        runOnUiThread {
            navController.navigate("${MainRoute.Team.path}/$HomeTeamId")
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.Team.route)
    }

    @Test
    fun mainScreen_navigateToPlayer() = inCompose {
        runOnUiThread {
            navController.navigate("${MainRoute.Player.path}/$HomePlayerId")
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.Player.route)
    }

    @Test
    fun mainScreen_navigateToCalendar() = inCompose {
        runOnUiThread {
            navController.navigate("${MainRoute.Calendar.path}/$FinalGameTimeMs")
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.Calendar.route)
    }

    @Test
    fun mainScreen_navigateToBet() = inCompose {
        runOnUiThread {
            navController.navigate("${MainRoute.Bet.path}/$UserAccount")
        }
        awaitIdle()
        navController.assertCurrentRoute(MainRoute.Bet.route)
    }
}
