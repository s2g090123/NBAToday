package com.jiachian.nbatoday.navigation

import com.jiachian.nbatoday.event.EventBroadcaster
import com.jiachian.nbatoday.event.EventManager

/**
 * Handle navigation events within the app.
 *
 * @param eventManager The event manager used for broadcasting navigation events.
 */
class NavigationController(
    private val eventManager: EventManager<Event> = EventManager()
) : EventBroadcaster<NavigationController.Event> by eventManager {
    sealed class Event {
        object NavigateToHome : Event()
        class NavigateToBoxScore(val gameId: String) : Event()
        class NavigateToTeam(val teamId: Int) : Event()
        class NavigateToPlayer(val playerId: Int) : Event()
        class NavigateToCalendar(val dateTime: Long) : Event()
        class NavigateToBet(val account: String) : Event()
        class BackScreen(val departure: MainRoute) : Event()
    }

    private fun Event.send() {
        eventManager.send(this)
    }

    fun navigateToHome() {
        Event.NavigateToHome.send()
    }

    fun navigateToBoxScore(gameId: String) {
        Event.NavigateToBoxScore(gameId).send()
    }

    fun navigateToTeam(teamId: Int) {
        Event.NavigateToTeam(teamId).send()
    }

    fun navigateToPlayer(playerId: Int) {
        Event.NavigateToPlayer(playerId).send()
    }

    fun navigateToCalendar(dateTime: Long) {
        Event.NavigateToCalendar(dateTime).send()
    }

    fun navigateToBet(account: String) {
        Event.NavigateToBet(account).send()
    }

    fun backScreen(departure: MainRoute) {
        Event.BackScreen(departure).send()
    }
}
