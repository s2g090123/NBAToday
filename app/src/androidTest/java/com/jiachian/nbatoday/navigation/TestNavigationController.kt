package com.jiachian.nbatoday.navigation

import com.jiachian.nbatoday.home.schedule.ui.model.DateData
import com.jiachian.nbatoday.main.ui.navigation.NavigationController

class TestNavigationController : NavigationController {
    var toHome: Boolean? = null
        private set

    var toPlayer: Int? = null
        private set

    var toBoxScore: String? = null
        private set

    var toTeam: Int? = null
        private set

    var toCalendar: String? = null
        private set

    var toBet: String? = null
        private set

    var showLoginDialog: Boolean? = null
        private set

    var showBetDialog: String? = null
        private set

    var back: Boolean? = null
        private set

    override fun navigateToHome() {
        toHome = true
    }

    override fun navigateToPlayer(playerId: Int) {
        toPlayer = playerId
    }

    override fun navigateToBoxScore(gameId: String) {
        toBoxScore = gameId
    }

    override fun navigateToTeam(teamId: Int) {
        toTeam = teamId
    }

    override fun navigateToCalendar(date: DateData) {
        toCalendar = date.dateString
    }

    override fun navigateToBet(account: String) {
        toBet = account
    }

    override fun showLoginDialog() {
        showLoginDialog = true
    }

    override fun showBetDialog(gameId: String) {
        showBetDialog = gameId
    }

    override fun back() {
        back = true
    }
}
