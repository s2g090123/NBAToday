package com.jiachian.nbatoday.compose.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jiachian.nbatoday.compose.screen.home.navigation.HomePage
import com.jiachian.nbatoday.compose.screen.home.schedule.SchedulePage
import com.jiachian.nbatoday.compose.screen.home.standing.StandingPage
import com.jiachian.nbatoday.compose.screen.home.user.UserPage
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.HomeTestTag

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController().apply {
        enableOnBackPressed(false)
    },
    navigationController: NavigationController,
) {
    Scaffold(
        bottomBar = {
            HomeBottomNavigation(
                pages = HomePage.values(),
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
                SchedulePage(
                    navigationController = navigationController
                )
            }
            composable(HomePage.STANDING.route) {
                StandingPage(
                    navigationController = navigationController
                )
            }
            composable(HomePage.USER.route) {
                UserPage(
                    modifier = Modifier.fillMaxSize(),
                    navigateToBet = navigationController::navigateToBet,
                    showLoginDialog = navigationController::showLoginDialog,
                )
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
                modifier = Modifier.testTag(HomeTestTag.HomeBottomNavigation_BottomNavigationItem),
                selected = page.route == currentRoute,
                alwaysShowLabel = page.route == currentRoute,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.primary,
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
