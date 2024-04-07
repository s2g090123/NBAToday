package com.jiachian.nbatoday.main.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.jiachian.nbatoday.bet.ui.main.BetScreen
import com.jiachian.nbatoday.bet.ui.main.BetViewModel
import com.jiachian.nbatoday.boxscore.ui.main.BoxScoreScreen
import com.jiachian.nbatoday.boxscore.ui.main.BoxScoreViewModel
import com.jiachian.nbatoday.calendar.ui.CalendarScreen
import com.jiachian.nbatoday.calendar.ui.CalendarViewModel
import com.jiachian.nbatoday.common.data.Transparency25
import com.jiachian.nbatoday.common.ui.bet.BetDialog
import com.jiachian.nbatoday.common.ui.bet.BetDialogViewModel
import com.jiachian.nbatoday.common.ui.login.LoginDialog
import com.jiachian.nbatoday.common.ui.login.LoginDialogViewModel
import com.jiachian.nbatoday.home.main.ui.HomeScreen
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.main.ui.navigation.NavigationController
import com.jiachian.nbatoday.main.ui.navigation.rememberNavigationController
import com.jiachian.nbatoday.player.ui.PlayerScreen
import com.jiachian.nbatoday.player.ui.PlayerViewModel
import com.jiachian.nbatoday.splash.ui.SplashScreen
import com.jiachian.nbatoday.splash.ui.SplashViewModel
import com.jiachian.nbatoday.team.ui.main.TeamScreen
import com.jiachian.nbatoday.team.ui.main.TeamViewModel
import org.koin.androidx.compose.koinViewModel

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
        splashRoute(navigationController)
        homeRoute(navigationController)
        boxScoreRoute(navigationController)
        teamRoute(navigationController)
        playerRoute(navigationController)
        calendarRoute(navigationController)
        betRoute(navigationController)
        loginDialog { navController.popBackStack() }
        betDialog { navController.popBackStack() }
    }
}

private fun NavGraphBuilder.splashRoute(
    navigationController: NavigationController,
) {
    composable(MainRoute.Splash.route) {
        val viewModel = koinViewModel<SplashViewModel>()
        SplashScreen(
            state = viewModel.state,
            colors = listOf(
                MaterialTheme.colors.secondary.copy(Transparency25),
                MaterialTheme.colors.secondary
            ),
            navigationController = navigationController,
        )
    }
}

private fun NavGraphBuilder.homeRoute(
    navigationController: NavigationController,
) {
    composable(MainRoute.Home.route) {
        HomeScreen(navigationController = navigationController)
    }
}

private fun NavGraphBuilder.boxScoreRoute(
    navigationController: NavigationController,
) {
    composable(MainRoute.BoxScore.route) {
        val viewModel = koinViewModel<BoxScoreViewModel>()
        BoxScoreScreen(
            state = viewModel.state,
            onEvent = viewModel::onEvent,
            navigationController = navigationController,
        )
    }
}

private fun NavGraphBuilder.teamRoute(
    navigationController: NavigationController,
) {
    composable(MainRoute.Team.route) {
        val viewModel = koinViewModel<TeamViewModel>()
        TeamScreen(
            state = viewModel.state,
            colors = viewModel.colors,
            onEvent = viewModel::onEvent,
            navigationController = navigationController
        )
    }
}

private fun NavGraphBuilder.playerRoute(
    navigationController: NavigationController,
) {
    composable(MainRoute.Player.route) {
        val viewModel = koinViewModel<PlayerViewModel>()
        PlayerScreen(
            state = viewModel.state,
            onEvent = viewModel::onEvent,
            navigationController = navigationController
        )
    }
}

private fun NavGraphBuilder.calendarRoute(
    navigationController: NavigationController,
) {
    composable(MainRoute.Calendar.route) {
        val viewModel = koinViewModel<CalendarViewModel>()
        CalendarScreen(
            state = viewModel.state,
            onEvent = viewModel::onEvent,
            navigationController = navigationController,
        )
    }
}

private fun NavGraphBuilder.betRoute(
    navigationController: NavigationController,
) {
    composable(MainRoute.Bet.route) {
        val viewModel = koinViewModel<BetViewModel>()
        BetScreen(
            state = viewModel.state,
            onEvent = viewModel::onEvent,
            navigationController = navigationController,
        )
    }
}

private fun NavGraphBuilder.loginDialog(
    onDismiss: () -> Unit,
) {
    dialog(MainRoute.LoginDialog.route) {
        val viewModel = koinViewModel<LoginDialogViewModel>()
        LoginDialog(
            state = viewModel.state,
            onEvent = viewModel::onEvent,
            onDismiss = onDismiss,
        )
    }
}

private fun NavGraphBuilder.betDialog(
    onDismiss: () -> Unit,
) {
    dialog(MainRoute.BetDialog.route) {
        val viewModel = koinViewModel<BetDialogViewModel>()
        BetDialog(
            state = viewModel.state,
            onEvent = viewModel::onEvent,
            onDismiss = onDismiss,
        )
    }
}
