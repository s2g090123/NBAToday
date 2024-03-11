package com.jiachian.nbatoday.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData
import java.text.SimpleDateFormat
import java.util.TimeZone

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

    @SuppressLint("SimpleDateFormat")
    fun navigateToCalendar(date: DateData) {
        SimpleDateFormat("yyyy/MM/dd").let { format ->
            format.timeZone = TimeZone.getTimeZone("EST")
            format.parse(date.dateString)?.time
        }?.let {
            navController.navigate(MainRoute.Calendar.build(it))
        }
    }
}

@Composable
fun rememberNavigationController(navHostController: NavHostController): NavigationController2 {
    return remember(navHostController) {
        NavigationController2(navHostController)
    }
}
