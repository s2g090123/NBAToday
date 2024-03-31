package com.jiachian.nbatoday.main.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.jiachian.nbatoday.bet.ui.main.BetScreen
import com.jiachian.nbatoday.boxscore.ui.main.BoxScoreScreen
import com.jiachian.nbatoday.calendar.ui.CalendarScreen
import com.jiachian.nbatoday.common.data.Transparency25
import com.jiachian.nbatoday.common.ui.bet.BetDialog
import com.jiachian.nbatoday.common.ui.login.LoginDialog
import com.jiachian.nbatoday.home.main.ui.HomeScreen
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.main.ui.navigation.NavigationController
import com.jiachian.nbatoday.main.ui.navigation.rememberNavigationController
import com.jiachian.nbatoday.player.ui.PlayerScreen
import com.jiachian.nbatoday.splash.ui.SplashScreen
import com.jiachian.nbatoday.team.ui.main.TeamScreen

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    navigationController: NavigationController = rememberNavigationController(navController),
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = MainRoute.Splash.route,
    ) {
        composable(MainRoute.Splash.route) {
            SplashScreen(
                colors = listOf(
                    MaterialTheme.colors.secondary.copy(Transparency25),
                    MaterialTheme.colors.secondary
                ),
                navigationController = navigationController,
            )
        }
        composable(MainRoute.Home.route) {
            HomeScreen(
                navigationController = navigationController
            )
        }
        composable(MainRoute.BoxScore.route) {
            BoxScoreScreen(
                navigationController = navigationController
            )
        }
        composable(MainRoute.Team.route) {
            TeamScreen(
                navigationController = navigationController
            )
        }
        composable(MainRoute.Player.route) {
            PlayerScreen(
                navigationController = navigationController
            )
        }
        composable(MainRoute.Calendar.route) {
            CalendarScreen(
                navigationController = navigationController
            )
        }
        composable(MainRoute.Bet.route) {
            BetScreen(
                navigationController = navigationController
            )
        }
        dialog(MainRoute.LoginDialog.route) {
            LoginDialog {
                navController.popBackStack()
            }
        }
        dialog(MainRoute.BetDialog.route) {
            BetDialog {
                navController.popBackStack()
            }
        }
    }
}
