package com.jiachian.nbatoday.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import java.text.SimpleDateFormat
import java.util.TimeZone

class NBANavigationController(
    private val navController: NavHostController,
) : NavigationController {
    override fun navigateToHome() {
        navController.navigate(MainRoute.Home.build()) {
            popUpTo(MainRoute.Splash.build()) {
                inclusive = true
            }
        }
    }

    override fun navigateToPlayer(playerId: Int) {
        navController.navigate(MainRoute.Player.build(playerId))
    }

    override fun navigateToBoxScore(gameId: String) {
        navController.navigate(MainRoute.BoxScore.build(gameId))
    }

    override fun navigateToTeam(teamId: Int) {
        navController.navigate(MainRoute.Team.build(teamId))
    }

    @SuppressLint("SimpleDateFormat")
    override fun navigateToCalendar(date: DateData) {
        SimpleDateFormat("yyyy/MM/dd").let { format ->
            format.timeZone = TimeZone.getTimeZone("EST")
            format.parse(date.dateString)?.time
        }?.let {
            navController.navigate(MainRoute.Calendar.build(it))
        }
    }

    override fun navigateToBet(account: String) {
        navController.navigate(MainRoute.Bet.build(account))
    }

    override fun showLoginDialog() {
        navController.navigate(MainRoute.LoginDialog.build())
    }

    override fun showBetDialog(gameId: String) {
        navController.navigate(MainRoute.BetDialog.build(gameId))
    }

    override fun back() {
        navController.popBackStack()
    }
}

@Composable
fun rememberNavigationController(navHostController: NavHostController): NBANavigationController {
    return remember(navHostController) {
        NBANavigationController(navHostController)
    }
}
