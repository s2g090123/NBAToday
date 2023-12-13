package com.jiachian.nbatoday.compose.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jiachian.nbatoday.compose.screen.home.navigation.HomePage
import com.jiachian.nbatoday.compose.screen.home.schedule.SchedulePage
import com.jiachian.nbatoday.compose.screen.home.standing.StandingPage
import com.jiachian.nbatoday.compose.screen.home.user.UserPage

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            HomeBottomNavigation(
                pages = viewModel.pages,
                navController = navController,
            )
        }
    ) { padding ->
        NavHost(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(padding),
            navController = navController,
            startDestination = HomePage.SCHEDULE.route,
        ) {
            composable(HomePage.SCHEDULE.route) {
                SchedulePage(viewModel = viewModel.schedulePageViewModel)
            }
            composable(HomePage.STANDING.route) {
                StandingPage(viewModel = viewModel.standingPageViewModel)
            }
            composable(HomePage.USER.route) {
                UserPage(modifier = Modifier, viewModel = viewModel.userPageViewModel)
            }
        }
    }
}

@Composable
private fun HomeBottomNavigation(
    pages: Array<HomePage>,
    navController: NavController,
) {
    val navBackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackEntry?.destination?.route
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.secondary,
    ) {
        pages.forEach { page ->
            BottomNavigationItem(
                selected = page.route == currentRoute,
                selectedContentColor = MaterialTheme.colors.primary,
                onClick = {
                    navController.navigate(page.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(page.iconRes),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(text = stringResource(page.labelRes))
                }
            )
        }
    }
}
