package com.jiachian.nbatoday.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController

class NavigationController2(
    private val navController: NavHostController,
) {
    fun navigateToPlayer(playerId: Int) {
        navController.navigate(MainRoute.Player.build(playerId))
    }

    fun navigateToBoxScore(gameId: String) {
        navController.navigate(MainRoute.BoxScore.build(gameId))
    }

    fun navigateToTeam(teamId: Int) {
        navController.navigate(MainRoute.Team.build(teamId))
    }
}

@Composable
fun rememberNavigationController(navHostController: NavHostController): NavigationController2 {
    return remember(navHostController) {
        NavigationController2(navHostController)
    }
}
