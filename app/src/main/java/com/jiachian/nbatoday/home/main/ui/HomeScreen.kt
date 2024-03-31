package com.jiachian.nbatoday.home.main.ui

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jiachian.nbatoday.home.main.ui.navigation.HomeRoute
import com.jiachian.nbatoday.home.schedule.ui.SchedulePage
import com.jiachian.nbatoday.home.standing.ui.StandingPage
import com.jiachian.nbatoday.home.user.ui.UserPage
import com.jiachian.nbatoday.main.ui.navigation.NavigationController
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
                pages = HomeRoute.values(),
                navController = navController,
            )
        }
    ) { padding ->
        NavHost(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(padding),
            navController = navController,
            startDestination = HomeRoute.SCHEDULE.route,
        ) {
            composable(HomeRoute.SCHEDULE.route) {
                SchedulePage(
                    navigationController = navigationController
                )
            }
            composable(HomeRoute.STANDING.route) {
                StandingPage(
                    navigationController = navigationController
                )
            }
            composable(HomeRoute.USER.route) {
                UserPage(
                    navigationController = navigationController
                )
            }
        }
    }
}

@Composable
private fun HomeBottomNavigation(
    pages: Array<HomeRoute>,
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
