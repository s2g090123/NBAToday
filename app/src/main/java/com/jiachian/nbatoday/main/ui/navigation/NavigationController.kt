package com.jiachian.nbatoday.main.ui.navigation

import com.jiachian.nbatoday.home.schedule.ui.model.DateData

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
