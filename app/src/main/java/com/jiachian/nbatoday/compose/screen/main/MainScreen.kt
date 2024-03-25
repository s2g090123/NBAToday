package com.jiachian.nbatoday.compose.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.jiachian.nbatoday.Transparency25
import com.jiachian.nbatoday.compose.screen.account.LoginDialog
import com.jiachian.nbatoday.compose.screen.bet.BetScreen
import com.jiachian.nbatoday.compose.screen.bet.dialog.BetDialog
import com.jiachian.nbatoday.compose.screen.calendar.CalendarScreen
import com.jiachian.nbatoday.compose.screen.home.HomeScreen
import com.jiachian.nbatoday.compose.screen.player.PlayerScreen
import com.jiachian.nbatoday.compose.screen.score.BoxScoreScreen
import com.jiachian.nbatoday.compose.screen.splash.SplashScreen
import com.jiachian.nbatoday.compose.screen.team.TeamScreen
import com.jiachian.nbatoday.navigation.MainRoute
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.navigation.rememberNavigationController

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
                openPlayerInfo = navigationController::navigateToPlayer,
                onBack = navController::popBackStack
            )
        }
        composable(MainRoute.Team.route) {
            TeamScreen(
                navigateToPlayer = navigationController::navigateToPlayer,
                navigateToBoxScore = navigationController::navigateToBoxScore,
                showLoginDialog = navigationController::showLoginDialog,
                showBetDialog = navigationController::showBetDialog,
                onBack = navController::popBackStack
            )
        }
        composable(MainRoute.Player.route) {
            PlayerScreen(
                onBack = navController::popBackStack,
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
