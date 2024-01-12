package com.jiachian.nbatoday.navigation

/**
 * Different routes in the main navigation flow.
 *
 * @param path The path for the route.
 * @param param The optional parameter associated with the route.
 */
sealed class MainRoute(
    val path: String,
    val param: String = "",
) {
    object Splash : MainRoute("splash")
    object Home : MainRoute("home")
    object BoxScore : MainRoute("box_score", "gameId")
    object Team : MainRoute("team", "teamId")
    object Player : MainRoute("player", "playerId")
    object Calendar : MainRoute("calendar", "dateTime")
    object Bet : MainRoute("bet", "account")

    // The full route string, including the parameter if present
    val route = if (param.isEmpty()) path else "$path/{$param}"
}
