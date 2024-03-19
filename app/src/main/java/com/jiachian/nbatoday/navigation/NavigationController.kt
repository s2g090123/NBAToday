package com.jiachian.nbatoday.navigation

import com.jiachian.nbatoday.compose.screen.home.schedule.models.DateData

interface NavigationController {
    fun navigateToHome()
    fun navigateToPlayer(playerId: Int)
    fun navigateToBoxScore(gameId: String)
    fun navigateToTeam(teamId: Int)
    fun navigateToCalendar(date: DateData)
    fun navigateToBet(account: String)
    fun showLoginDialog()
    fun showBetDialog(gameId: String)
    fun back()
}
